package com.telstra.mobile.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.telstra.mobile.myproficiency.R;


public class Config {

    public static boolean isNetworkAvailable(final Context context) {
        try {
            ConnectivityManager connection = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo Wifi = connection
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (Wifi != null && Wifi.isConnectedOrConnecting())
                return true;

            NetworkInfo Mobile = connection
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (Mobile != null && Mobile.isConnectedOrConnecting())
                return true;

            NetworkInfo activeNet = connection.getActiveNetworkInfo();
            if (activeNet != null && activeNet.isConnectedOrConnecting())
                return true;

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.check_network_connection,
                            Toast.LENGTH_SHORT).show();
                }
            });

            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
