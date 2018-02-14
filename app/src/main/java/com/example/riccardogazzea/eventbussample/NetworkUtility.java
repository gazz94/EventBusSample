package com.example.riccardogazzea.eventbussample;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created on 14/02/2018.
 *
 * @author riccardogazzea
 */

public class NetworkUtility {

    public static int TYPE_NONE = -1; //ConnectivityManager.NONE;
    public static int TYPE_MOBILE = ConnectivityManager.TYPE_MOBILE;
    public static int TYPE_WIFI = ConnectivityManager.TYPE_WIFI;
    /**
     * Checks if the network is available.
     *
     * NB: It doesn't check if the device can actually connect to the internet.
     * The best way to check if there is an Internet connection present is to try and connect to some known server using HTTP
     *
     * @param context the calling {@link Context}.
     * @return {@code true} if exists a network connection, {@code false} otherwise
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isAvailable = false;
        if (manager != null){
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // Network is present and connected
                isAvailable = true;
            }
        }
        return isAvailable;
    }

    public static int getNetworkType(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int networkType = -1; //ConnectivityManager.TYPE_NONE;
        if (manager != null){
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                // Network is present and connected
                networkType = networkInfo.getType();
            }
        }
        return networkType;
    }
}
