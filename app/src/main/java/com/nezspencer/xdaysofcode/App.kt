package com.nezspencer.xdaysofcode

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by reach1 on 1/22/18.
 */
class App : Application(){

    companion object {
        val retrofit: Retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .baseUrl(Constants.API_BASE_URL)
                    .build()
    }

    override fun onCreate() {
        super.onCreate()
    }


}