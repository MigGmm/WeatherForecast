package com.miguelgarcia.android.weatherforecast.service;


import android.util.Log;

import com.miguelgarcia.android.weatherforecast.object.ShowError;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * CLass who serving the data load form world weather online server.
 *
 * @author Miguel Francisco García del Moral Muñoz.
 */
public class JSONWeatherForecastRequest {

    /**
     * Method who return data from server.
     *
     * @param url_server - String for load data.
     * @return - JSONObject whit data for five days weather prediction and live.
     */
    public JSONObject getDataJSON(String url_server) {
        InputStream inputStream = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url_server));
            inputStream = httpResponse.getEntity().getContent();
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        return parseJSONString(inputStream);
    }

    /**
     * Create the JSONObject from a InputStream object whit forecast information.
     *
     * @param inputStream - InputInformation whit the information retrieved from server.
     * @return - JSONObject or null if the process fail.
     */
    private JSONObject parseJSONString (InputStream inputStream) {
        String result = "";
        if (inputStream!=null){
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();
                result = sb.toString();
            }catch(Exception e){
                return  null;
            }
            try{
                JSONObject jArray = new JSONObject(result);
                return jArray;
            }
            catch(JSONException e){
                return null;
            }
        }
        else{
            return null;
        }
    }
}

