package com.example.bigschoolexample.data.remote

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://thesimpsonsapi.com/api/"

    @Volatile
    private var retrofit: Retrofit? = null

    @Volatile
    private var simpsonsApiService: SimpsonsApiService? = null

    fun getSimpsonsApiService(context: Context): SimpsonsApiService {
        return simpsonsApiService ?: synchronized(this) {
            simpsonsApiService ?: buildRetrofit(context.applicationContext)
                .create(SimpsonsApiService::class.java)
                .also { simpsonsApiService = it }
        }
    }

    private fun buildRetrofit(context: Context): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(buildOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .also { retrofit = it }
        }
    }

    private fun buildOkHttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ConnectivityInterceptor(context))
            .build()
    }
}
