package com.miguelgarcia.android.weatherforecast.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.miguelgarcia.android.weatherforecast.R;

/**
 * Activity who extends PreferencesActivity, allows the user set units for temperature and length.
 */
public class PreferencesActivity extends PreferenceActivity {


    public PreferencesActivity () {
        // Constructor must be empty.
    }

    /**
     * Called when PreferencesActivity is instantiated.
     *
     * @param savedInstanceState - User's instanced state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        try {
            getActionBar().setDisplayShowCustomEnabled(true);
            getActionBar().setCustomView(R.layout.action_bar_title);
            getActionBar().setIcon(R.drawable.ab_solid_example);
            TextView aux = (TextView) findViewById(R.id.my_action_bar_title);
            aux.setText(getText(R.string.action_settings));
        } catch (Exception e){}
    }

    /**
     * Listener for action bar arrow.
     *
     * @param item - Item menu selected.
     * @return - True if a item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }
}
