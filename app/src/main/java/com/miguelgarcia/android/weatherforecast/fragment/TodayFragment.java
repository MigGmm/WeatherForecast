package com.miguelgarcia.android.weatherforecast.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguelgarcia.android.weatherforecast.R;
import com.miguelgarcia.android.weatherforecast.activity.MainActivity;
import com.miguelgarcia.android.weatherforecast.object.ForecastDay;

/**
 * Class who extends Fragment for present day weather information.
 *
 * @author Miguel Francisco García del Moral Muñoz.
 */
public class TodayFragment extends Fragment {

    private TextView tvLocation, tvTemperature, tvWeatherdesc, tvHumidity, tvPrecipitation, tvPressure, tvWindSpeed, tvDirection;
    private ImageView ivWeatherIcon;
    private ForecastDay today;
    private String lengthPreferences;
    private String temperaturePreferences;
    private Button btnRestart;

    public TodayFragment() {
        // Constructor must be empty.
    }

    /**
     * Method called when a TodayFragment is instantiated.
     *
     * @param inflater - Compressed data.
     * @param container - Container who load the fragment.
     * @param savedInstanceState - User's saved instance.
     * @return - the view created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    /**
     * When the instanced is created.
     *
     * @param savedInstanceState - User's saved instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadViews();
        loadPreferences();
        getActivity().getActionBar().setTitle(R.string.fragment_today);
        // Check if located
        if (MainActivity.isLocated()) {
            btnRestart.setVisibility(View.INVISIBLE);
            // check today information
           if (today != null) {
                rederViews();
            }
        } else {
        }
    }

    /**
     * Load all views into the fragment.
     */
    public void loadViews() {
        ivWeatherIcon = (ImageView) getView().findViewById(R.id.ivWeatherToday);
        tvLocation = (TextView) getView().findViewById(R.id.tvLocationToday);
        tvTemperature = (TextView) getView().findViewById(R.id.tvTemperatireToday);
        tvWeatherdesc = (TextView) getView().findViewById(R.id.tvWeatherDesc);
        tvHumidity = (TextView) getView().findViewById(R.id.tvHumidityToday);
        tvPrecipitation = (TextView) getView().findViewById(R.id.tvPrecipitationToday);
        tvPressure = (TextView) getView().findViewById(R.id.tvPressureToday);
        tvWindSpeed = (TextView) getView().findViewById(R.id.tvWindSpeedToday);
        tvDirection = (TextView) getView().findViewById(R.id.tvDirectionToday);
        btnRestart = (Button) getView().findViewById(R.id.btnRestart);
    }

    /**
     * App load the user's preferences saved.
     */
    private void loadPreferences() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        lengthPreferences = preferences.getString("unitLength", "0");
        temperaturePreferences = preferences.getString("unitTemperature", "0");
    }

    /**
     * Paint all the information in each section.
     */
    private void rederViews() {
        ivWeatherIcon.setImageBitmap(today.getWeatherIcon());
        tvLocation.setText(today.getLocation());
        if (temperaturePreferences.equals("0")) tvTemperature.setText(today.getTempC() + " ");
        else if (temperaturePreferences.equals("1")) tvTemperature.setText(today.getTempF() + " ");
        tvWeatherdesc.setText(" " + today.getWeatherDesc());
        tvHumidity.setText(today.getHumidity());
        tvPrecipitation.setText(today.getPrecipitation());
        tvPressure.setText(today.getPressure());
        if (lengthPreferences.equals("0")) tvWindSpeed.setText(today.getWindSpeedKmh());
        else if (lengthPreferences.equals("1")) tvWindSpeed.setText(today.getWindSpeedMph());
        tvDirection.setText(today.getDirection());
    }

    /**
     * Method for receive information from MainActivity.
     *
     * @param today - Today's information.
     */
    public void setToday (ForecastDay today) {
        this.today = today;
    }
}
