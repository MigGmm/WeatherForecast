package com.miguelgarcia.android.weatherforecast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.miguelgarcia.android.weatherforecast.R;
import com.miguelgarcia.android.weatherforecast.object.NavegationDrawerItem;

import java.util.ArrayList;

/**
 * Class who extends BaseAdapter used for create navigation drawer items.
 */
public class DrawerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavegationDrawerItem> navDrawerItems;

    /**
     * Constructor give the necessary information as parameters.
     *
     * @param context - MainActivity's context.
     * @param navDrawerItems - ArrayList<NavegationDrawerItem> with the navigation drawer items.
     */
    public DrawerAdapter(Context context, ArrayList<NavegationDrawerItem> navDrawerItems) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    /**
     * Return te size of the navigation drawer given by the ArrayList size.
     *
     * @return - Size.
     */
    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    /**
     * Return the item at position.
     *
     * @param position - Position's item.
     * @return - Item.
     */
    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
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
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }
        ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIconDrawer);
        TextView tvTittle = (TextView) convertView.findViewById(R.id.tvDrawerText);
        ivIcon.setImageResource(navDrawerItems.get(position).getIcon());
        tvTittle.setText(navDrawerItems.get(position).getTittle());
        return convertView;
    }
}
