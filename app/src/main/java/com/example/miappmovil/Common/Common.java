package com.example.miappmovil.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.miappmovil.Model.User;

import java.util.Arrays;

public class Common {
    public static User currentUser;
    public static final String DELETE = "Eliminar";
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            Log.d("INFO1", "" + Arrays.toString(info));

            if (info != null) {

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        Log.d("INFO2", "" + info[i].getState());
                    return true;
                }
            }
        }

        return false;
    }

    public static String convertCodeToStatus(String status) {
        if (status.equals("0"))
            return "Colocado";
        else if (status.equals("1"))
            return "En camino";
        else
            return  "Enviada";
    }

}
