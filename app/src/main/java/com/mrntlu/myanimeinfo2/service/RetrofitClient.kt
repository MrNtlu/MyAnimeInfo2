package com.mrntlu.myanimeinfo2.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {

    companion object{
        fun getClient(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v3/")
                .addConverterFactory(GsonConverterFactory.create()).build()
        }
    }
}