package com.androidgifts.gift.recyclerviewwithcustomadapter.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Stas on 01.07.2016.
 */
public class NetworkConnections {
    public static Boolean networkcheck(Context context){

        Boolean returnValue = false; // Initial Value

        ConnectivityManager manager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            returnValue = true;

        }

        return returnValue;

    }
}

