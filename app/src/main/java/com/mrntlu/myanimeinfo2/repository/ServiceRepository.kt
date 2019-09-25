package com.mrntlu.myanimeinfo2.repository

import android.app.Application
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.service.AnimeService
import com.mrntlu.myanimeinfo2.service.RetrofitClient
import retrofit2.Response

class ServiceRepository(application: Application) {

    private val apiClient=RetrofitClient.getClient().create(AnimeService::class.java)

    //Anime
    suspend fun getAnimeByID(mal_id:Int): AnimeResponse{
        return apiClient.getAnimeByID(mal_id)
    }

    suspend fun getAnimeCharactersByID(mal_id: Int):CharactersResponse{
        return apiClient.getAnimeCharactersByID(mal_id)
    }

    suspend fun getTopAnimes(page:Int,subtype:String):TopAnimeResponse{
        return apiClient.getTopAnimes(page,subtype)
    }

    suspend fun getAnimeSchedule(): AnimeScheduleResponse{
        return apiClient.getAnimeSchedule()
    }

    suspend fun getAnimeByGenre(genreID:Int,page:Int): AnimeGenreSeasonResponse{
        return apiClient.getAnimeByGenre(genreID,page)
    }

    suspend fun getAnimeBySeason(year:Int,season:String):AnimeGenreSeasonResponse{
        return apiClient.getAnimeBySeason(year,season)
    }

    suspend fun getAnimeBySearch(q:String,page:Int):AnimeSearchResponse{
        return apiClient.getAnimeBySearch(q,page)
    }

    //Manga
    suspend fun getMangaByID(mal_id:Int):MangaResponse{
        return apiClient.getMangaByID(mal_id)
    }

    suspend fun getMangaReviewsByID(mal_id: Int,page: Int):ReviewsResponse{
        return apiClient.getMangaReviewsByID(mal_id,page)
    }

    suspend fun getTopMangas(page:Int,subtype:String):TopMangaResponse{
        return apiClient.getTopMangas(page,subtype)
    }

    suspend fun getMangaCharactersByID(mal_id: Int):CharactersResponse{
        return apiClient.getMangaCharactersByID(mal_id)
    }

    suspend fun getMangaByGenre(genreID:Int,page:Int):MangaGenreSeasonResponse{
        return apiClient.getMangaByGenre(genreID,page)
    }

    suspend fun getMangaBySearch(q:String, page:Int):MangaSearchResponse{
        return apiClient.getMangaBySearch(q,page)
    }

    //Common
    suspend fun getRecommendationsByID(type:String,mal_id:Int):RecommendationsResponse{
        return apiClient.getRecommendationsByID(type,mal_id)
    }

    suspend fun getCharacterInfoByID(character_id:Int):CharacterInfoResponse{
        return apiClient.getCharacterInfoByID(character_id)
    }

    suspend fun getReviewsByID(type:String,mal_id:Int,page:Int):ReviewsResponse{
        return apiClient.getReviewsByID(type,mal_id,page)
    }

    suspend fun getPicturesByID(type:String,mal_id:Int):PicturesResponse{
        return apiClient.getPicturesByID(type,mal_id)
    }
}