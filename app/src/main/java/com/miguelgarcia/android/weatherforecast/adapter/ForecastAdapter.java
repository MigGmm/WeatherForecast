package com.miguelgarcia.android.weatherforecast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguelgarcia.android.weatherforecast.R;
import com.miguelgarcia.android.weatherforecast.object.ForecastDay;

import java.util.ArrayList;

/**
 * class who extends BaseAdapter and is responsible to load the ForecastFragment's ListView.
 *
 * @author Miguel Francisco García del Moral Muñoz.
 */
public class ForecastAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ForecastDay> forecastWeek;
    private String temperaturePreferences;

    /**
     * Constructor give the necessary information as parameters.
     *
     * @param context - MainActivity's context.
     * @param forecastWeek - ArrayList<ForecastDay> with the weather forecast information per day.
     * @param temperaturePreferences - If must to paint Cº or Fº.
     */
    public ForecastAdapter(Context context, ArrayList<ForecastDay> forecastWeek, String temperaturePreferences) {
        this.context = context;
        this.forecastWeek = forecastWeek;
        this.temperaturePreferences = temperaturePreferences;
    }

    /**
     * Return te size of the navigation drawer given by the ArrayList size.
     *
     * @return - Size.
     */
    @Override
    public int getCount() {
        return forecastWeek.size();
    }

    /**
     * Return the item at position.
     *
     * @param position - Position's item.
     * @return - Item.
     */
    @Override
    public Object getItem(int position) {
        return forecastWeek.get(position);
    }

    /**
     * Return the item's ID, who matches position.
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Create each item whit the data.
     *
     * @param position - Position to retrieve the information.
     * @param convertView - View to convert whit the item information.
     * @param parent - Special view content the item views.
     * @return - The new view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView==null)
            view = inflater.inflate(R.layout.forecast_list_item, null,
                    true);
        ImageView ivWeatherIcon = (ImageView) view.findViewById(R.id.ivForecastIcon);
        TextView tvForecastDay = (TextView) view.findViewById(R.id.tvForecastDay);
        TextView tvForecastTemperature = (TextView) view.findViewById(R.id.tvForecastTemperature);
        TextView tvForecastWeatherDesc= (TextView) view.findViewById(R.id.tvForecastWeatherDesc);
        ivWeatherIcon.setImageBitmap(forecastWeek.get(position).getWeatherIcon());
        tvForecastDay.setText(forecastWeek.get(position).getWeekDay());
        if (temperaturePreferences.equals("1")) tvForecastTemperature.setText(forecastWeek.get(position).getTempF());
        else tvForecastTemperature.setText(forecastWeek.get(position).getTempC());
        tvForecastWeatherDesc.setText(forecastWeek.get(position).getWeatherDesc());
        return view;
    }
}
