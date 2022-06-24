package com.varun.grogu.data.remote

import com.varun.grogu.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Http {

    fun getRetrofitClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient().newBuilder()
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
        }
        return builder.build()
    }
}