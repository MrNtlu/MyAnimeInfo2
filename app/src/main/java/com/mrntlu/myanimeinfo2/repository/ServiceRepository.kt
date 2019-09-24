package com.mrntlu.myanimeinfo2.repository

import android.app.Application
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.service.AnimeService
import com.mrntlu.myanimeinfo2.service.RetrofitClient
import retrofit2.Response

class ServiceRepository(application: Application) {

    private val apiClient=RetrofitClient.getClient().create(AnimeService::class.java)

    suspend fun getAnimeByID(mal_id:Int): AnimeResponse{
        return apiClient.getAnimeByID(mal_id)
    }
}