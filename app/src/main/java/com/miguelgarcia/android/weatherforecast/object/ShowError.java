package com.miguelgarcia.android.weatherforecast.object;

import android.content.Context;
import android.widget.Toast;

/**
 * Class for easy error messages.
 */
public class ShowError {
    public static void ShowError(Context context,String error) {
        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
    }
}
