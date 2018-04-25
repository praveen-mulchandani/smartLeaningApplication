package com.sd.smartlearningapplication.utìls

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    fun checkNetwork(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}