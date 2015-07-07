/*
Weather forecast application. Its organized in a main activity who call fragments over a container (FrameLayout).
Here is two fragments and two other activities, preferences and about.
The two fragments are for today and forecast information.
 */

package com.miguelgarcia.android.weatherforecast.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.miguelgarcia.android.weatherforecast.R;
import com.miguelgarcia.android.weatherforecast.adapter.DrawerAdapter;
import com.miguelgarcia.android.weatherforecast.fragment.ForecastFragment;
import com.miguelgarcia.android.weatherforecast.fragment.TodayFragment;
import com.miguelgarcia.android.weatherforecast.geolocation.GeolocationService;
import com.miguelgarcia.android.weatherforecast.object.ForecastDay;
import com.miguelgarcia.android.weatherforecast.object.NavegationDrawerItem;
import com.miguelgarcia.android.weatherforecast.object.ShowError;
import com.miguelgarcia.android.weatherforecast.service.JSONWeatherForecastRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Main activity.
 *
 *@author Miguel Francisco García del Moral Muñoz
 */
public class MainActivity extends FragmentActivity {

    // Fields
    private static boolean located;
    private ListView leftListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private GeolocationService locationService;
    private String locality, country;
    private int fragmentAttached;
    private double latitude;
    private double longitude;
    private JSONWeatherForecastRequest jSONWeatherForecastRequest;
    private JSONObject jSONObject;
    private String fullLocation;
    private ForecastDay today;
    private ArrayList<ForecastDay> forecastWeek = new ArrayList<>();
    String[] weekDays;
    private TextView aux;

    /**
     * On create, this method is called when the application start.
     *
     * @param savedInstanceState - User`s saved app state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        located = false;
        loadCustomActionBar();
        loadViews();
        loadDrawer();
        loadListener();
        loadCoordinates();
        if (located) {
            if (isOnline()) new RequestJSON().execute();
            else ShowError.ShowError(this, getString(R.string.connection_needed));
        } else ShowError.ShowError(this, getString(R.string.cant_locate));
    }

    /**
     * Create the options menu.
     *
     * @param menu - Menu.
     * @return Return true once created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Called when activity start-up is complete. Mostly intended for framework use.
     *
     * @param savedInstanceState - User`s saved app state.
     */
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause(), for your activity to start interacting with the user.
     */
    @Override
    public void onResume() {
        super.onResume();
        loadCoordinates();
        if (fragmentAttached == 0) loadFragmentToday();
        else if (fragmentAttached == 1) loadFragmentForecast();
        else loadFragmentToday();
    }

    /**
     * I included that after read about a know bug, when the app was returning from About crashed.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    /**
     * When the user change setting parameters.
     *
     * @param newConfig - New parameters.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /**
     * Listener for menu.
     *
     * @param item - Item selected.
     *
     * @return - If a options is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (id) {
            case R.id.about_menu:
                Intent intentAbout = new Intent(this, AboutActivity.class);
                startActivity(intentAbout);
                return true;
            case R.id.action_settings:
                Intent intentPreferences = new Intent(this, PreferencesActivity.class);
                startActivity(intentPreferences);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method create a custom style for the action bar.
     */
    private void loadCustomActionBar() {
        try {
            getActionBar().setDisplayShowCustomEnabled(true);
            getActionBar().setCustomView(R.layout.action_bar_title);
            getActionBar().setIcon(R.drawable.ab_solid_example);
            aux = (TextView) findViewById(R.id.my_action_bar_title);
            aux.setText(getText(R.string.fragment_today));
        } catch (Exception e){}
    }

    /**
     * Load all views used in this activity.
     */
    private void loadViews() {
        leftListView = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    /**
     * Called when need to load a fragment into the container.
     */
    private void loadFragment() {
        if (fragmentAttached == 0) loadFragmentToday();
        else if (fragmentAttached == 1) loadFragmentForecast();
        else loadFragmentToday();
    }

    /**
     * Load TodayFragment into the container.
     */
    private void loadFragmentToday() {
        TodayFragment newFragment = new TodayFragment();
        newFragment.setToday(today);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentAttached = 0;
    }

    /**
     * Load ForecastFragment into the container.
     */
    private void loadFragmentForecast() {
        ForecastFragment newFragment = new ForecastFragment();
        newFragment.setWeekDays(forecastWeek);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        fragmentAttached = 1;
    }

    /**
     * Check the internet.
     *
     * @return - Return true if connection is available.
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Load the position of the device with latitude and longitude.
     */
    private void loadCoordinates() {
        locationService = new GeolocationService(
                this);
        // For network try.
        Location nwLocation = locationService
                .getLocation(LocationManager.NETWORK_PROVIDER);
        if (nwLocation != null) {
            latitude = nwLocation.getLatitude();
            longitude = nwLocation.getLongitude();
            loadLocality();
            located = true;
        } else {
            // If network fail. GPS try.
            Location gpsLocation = locationService
                    .getLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();
                loadLocality();
                located = true;
            }
        }
    }

    /**
     * Find out the locality and country from latitude and longitude.
     */
    private void loadLocality() {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {

        }
        if (addresses != null) {
            if (addresses.size() > 0) {
                locality = addresses.get(0).getLocality();
                country = addresses.get(0).getCountryName();
                fullLocation = locality + ", " + country;
            }
        } else {
            ShowError.ShowError(this, getString(R.string.cant_locate));
        }
    }

    /**
     * This method load the navigation drawer menu in the left of the activity.
     */
    private void loadDrawer() {
        final String[] tittles = getResources().getStringArray(
                R.array.navigation_drawer_items);
        ArrayList<NavegationDrawerItem> navDrawerItems = new ArrayList<>();
        navDrawerItems.add(new NavegationDrawerItem(tittles[0], R.drawable.ic_drawer_today_dark));
        navDrawerItems.add(new NavegationDrawerItem(tittles[1], R.drawable.ic_drawer_forecast_dark));
        leftListView.setAdapter(new DrawerAdapter(getApplicationContext(), navDrawerItems));
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,       // Drawer icon.
                R.string.drawer_open,
                R.string.fragment_today
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    /**
     * Set the listener for navigation drawer.
     */
    private void loadListener() {
        leftListView.setOnItemClickListener(new DrawerListener());
    }

    /**
     * Static method who determines if the device is located.
     *
     * @return - True if the device is located.
     */
    public static boolean isLocated() {return located;}

    /**
     * Method called from restart button.
     *
     * @param v - Restart button.
     */
    public void restartApplication (View v) {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    /**
     * Class who create a progress dialog while load all the weather data from the server.
     */
    public class RequestJSON extends AsyncTask<Void, Integer, Boolean> {

        private ProgressDialog progressDialog;

        /**
         * This method crate and show the dialog.
         */
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage(getString(R.string.getting_weather_forecast));
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        /**
         * It´s what the class do underground.
         * Basically get the weather information from world weather online.
         *
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            String urlServer = "http://api.worldweatheronline.com/free/v2/weather.ashx?q=" + latitude + "%20" + longitude + "&format=json&num_of_days=5&tp=24&key=0bf7beb89f44ce5348dd18617688f";
            jSONWeatherForecastRequest = new JSONWeatherForecastRequest();
            jSONObject = jSONWeatherForecastRequest.getDataJSON(urlServer);
            if (jSONObject != null) {
                loadTodayInformation();
                loadForecastInformation();
                return true;
            } else return false;
        }

        /**
         * Once the app have the data.
         * @param result - The result of doInBackground().
         */
        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (result == true) {
                loadFragment();
            } else {
                ShowError.ShowError(MainActivity.this, getString(R.string.error_getting_data));
            }
        }

        /**
         * Load into a ForecastDay variable the live information.
         */
        private void loadTodayInformation() {
            today = new ForecastDay();
            try {
                today.setLocation(locality + ", " + country);
                String imageUrl = jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherIconUrl").getJSONObject(0).get("value").toString();
                try {
                    today.setWeatherIcon(Picasso.with(MainActivity.this).load(imageUrl).get());
                } catch (IOException e) {
                    ShowError.ShowError(MainActivity.this, getString(R.string.error_getting_data));
                }
                today.setTempC(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("temp_C").toString());
                today.setTempF(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("temp_F").toString());
                today.setWeatherDesc(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).get("value").toString());
                today.setHumidity(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("humidity").toString());
                today.setPrecipitation(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("precipMM").toString());
                today.setPressure(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("pressure").toString());
                today.setWindSpeedKmh(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("windspeedKmph").toString());
                today.setWindSpeedMph(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("windspeedMiles").toString());
                today.setDirection(jSONObject.getJSONObject("data").getJSONArray("current_condition").getJSONObject(0).get("winddir16Point").toString());
            } catch (JSONException e) {
                today = null;
                ShowError.ShowError(MainActivity.this, getString(R.string.error_getting_data));
            }
        }

        /**
         * Load into a ArrayList<ForecastDay> the five days forecast.
         */
        private void loadForecastInformation() {
            weekDays  = getResources().getStringArray(R.array.week_days);
            for (int i = 0; i < 5; i++) {
                forecastWeek.add(generateWeekDay(i));
            }
        }

        /**
         * created each day information.
         *
         * @param position - Position on the ArrayList, which matches whit the position in json.
         *
         * @return - ForecastDay object with the data.
         */
        private ForecastDay generateWeekDay(int position) {
            ForecastDay day = new ForecastDay();
            try {
                //today.setLocation(locality + ", " + country);
                String imageUrl = jSONObject.getJSONObject("data").getJSONArray("weather").getJSONObject(position).getJSONArray("hourly").getJSONObject(0).getJSONArray("weatherIconUrl").getJSONObject(0).get("value").toString();
                try {
                    day.setWeatherIcon(Picasso.with(MainActivity.this).load(imageUrl).get());
                } catch (IOException e) {
                    ShowError.ShowError(MainActivity.this, getString(R.string.error_getting_data));
                }
                day.setTempC(jSONObject.getJSONObject("data").getJSONArray("weather").getJSONObject(position).getJSONArray("hourly").getJSONObject(0).get("tempC").toString());
                day.setTempF(jSONObject.getJSONObject("data").getJSONArray("weather").getJSONObject(position).getJSONArray("hourly").getJSONObject(0).get("tempF").toString());
                day.setWeatherDesc(jSONObject.getJSONObject("data").getJSONArray("weather").getJSONObject(position).getJSONArray("hourly").getJSONObject(0).getJSONArray("weatherDesc").getJSONObject(0).get("value").toString());
                day.setWeekDay(getWeekDay(position));
            } catch (JSONException e) {
                today = null;
                ShowError.ShowError(MainActivity.this, getString(R.string.error_getting_data));
            }
            return day;
        }

        /**
         * Calculates the day of the week from the system and return as String.
         *
         * @param position - Position on ArrayList.
         *
         * @return - String of corresponding day.
         */
        private String getWeekDay(int position) {
            int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) + position;
            while (day > 7) {
                day = day - 7;
            }
            return weekDays[day - 1];
        }
    }

    /**
     * Class who implement listeners for navigation drawer.
     */
    private class DrawerListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                    loadFragmentToday();
                    mDrawerLayout.closeDrawers();
                    aux.setText(R.string.fragment_today);
                    break;
                case 1:
                    loadFragmentForecast();
                    mDrawerLayout.closeDrawers();
                    aux.setText(R.string.fragment_forecast);
                    break;
            }
        }
    }
}
