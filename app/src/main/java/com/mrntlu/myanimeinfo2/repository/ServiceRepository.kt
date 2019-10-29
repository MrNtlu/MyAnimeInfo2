package com.mrntlu.myanimeinfo2.repository

import android.app.Application
import com.mrntlu.myanimeinfo2.service.AnimeService
import com.mrntlu.myanimeinfo2.service.RetrofitClient

class ServiceRepository(application: Application) {

    private val apiClient=RetrofitClient.getClient().create(AnimeService::class.java)

    //Anime
    suspend fun getAnimeByID(mal_id:Int)=apiClient.getAnimeByID(mal_id)

    suspend fun getAnimeCharactersByID(mal_id: Int)=apiClient.getAnimeCharactersByID(mal_id)

    suspend fun getTopAnimes(page:Int,subtype:String)=apiClient.getTopAnimes(page,subtype)

    suspend fun getAnimeSchedule()=apiClient.getAnimeSchedule()

    suspend fun getAnimeByGenre(genreID:Int,page:Int)=apiClient.getAnimeByGenre(genreID,page)

    suspend fun getAnimeBySeason(year:String,season:String)=apiClient.getAnimeBySeason(year,season)

    suspend fun getAnimeBySearch(q:String,page:Int)=apiClient.getAnimeBySearch(q,page)

    //Manga
    suspend fun getMangaByID(mal_id:Int)=apiClient.getMangaByID(mal_id)

    suspend fun getTopMangas(page:Int,subtype:String)=apiClient.getTopMangas(page,subtype)

    suspend fun getMangaCharactersByID(mal_id: Int)=apiClient.getMangaCharactersByID(mal_id)

    suspend fun getMangaByGenre(genreID:Int,page:Int)=apiClient.getMangaByGenre(genreID,page)

    suspend fun getMangaBySearch(q:String, page:Int)=apiClient.getMangaBySearch(q,page)

    //Common
    suspend fun getProducerInfoByID(mal_id: Int)=apiClient.getProducerInfoByID(mal_id)

    suspend fun getRecommendationsByID(type:String,mal_id:Int)=apiClient.getRecommendationsByID(type,mal_id)

    suspend fun getCharacterInfoByID(character_id:Int)=apiClient.getCharacterInfoByID(character_id)

    suspend fun getReviewsByID(type:String,mal_id:Int,page:Int)=apiClient.getReviewsByID(type,mal_id,page)

    suspend fun getPicturesByID(type:String,mal_id:Int)=apiClient.getPicturesByID(type,mal_id)

    //Others
    suspend fun getUserProfile(username:String)=apiClient.getUserProfile(username)

    suspend fun getUserAnimeList(username:String)=apiClient.getUserAnimeList(username)

    suspend fun getUserMangaList(username:String)=apiClient.getUserMangaList(username)
}