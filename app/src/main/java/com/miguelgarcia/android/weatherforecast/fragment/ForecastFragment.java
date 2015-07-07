package com.miguelgarcia.android.weatherforecast.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miguelgarcia.android.weatherforecast.R;
import com.miguelgarcia.android.weatherforecast.adapter.ForecastAdapter;
import com.miguelgarcia.android.weatherforecast.object.ForecastDay;

import java.util.ArrayList;

/**
 * Class who extends ListFragment for the weather forecast of five days from.
 *
 * @author Miguel Francisco García del Moral Muñoz
 */
public class ForecastFragment extends ListFragment {

    ArrayList<ForecastDay> weekDays;
    private String lengthPreferences;
    private String temperaturePreferences;

    public ForecastFragment() {
        // Constructor must be empty.
    }

    /**
     * Method called when a ForecastFragment is instantiated.
     *
     * @param inflater - Compressed data.
     * @param container - Container who load the fragment.
     * @param savedInstanceState - User's saved instance.
     * @return - the view created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    /**
     * When the instanced is created.
     *
     * @param savedInstanceState - User's saved instance.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadPreferences();
        TextView aux = (TextView) getActivity().findViewById(R.id.my_action_bar_title);
        aux.setText(getText(R.string.fragment_forecast));
        setListAdapter(new ForecastAdapter(getActivity(), weekDays, temperaturePreferences));
    }

    /**
     * Called after onRestoreInstanceState(Bundle), onRestart(), or onPause().
     */
    @Override
    public void onResume() {
        super.onResume();
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
     * Method for receive information from MainActivity.
     *
     * @param weekDays - ArrayList<ForecastDay> with forecast information.
     */
    public void setWeekDays(ArrayList<ForecastDay> weekDays) {
        this.weekDays = weekDays;
    }
}
