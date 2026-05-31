package com.example.bigschoolexample.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptor(
    context: Context,
) : Interceptor {

    private val appContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hasInternetConnection()) {
            throw NoInternetException()
        }

        return chain.proceed(chain.request())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = appContext.getSystemService(ConnectivityManager::class.java)
            ?: return false
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
