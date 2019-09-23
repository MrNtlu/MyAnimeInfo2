package com.mrntlu.myanimeinfo2.service

import com.mrntlu.myanimeinfo2.models.*
import retrofit2.http.GET
import retrofit2.http.Path

interface AnimeService {

    //https://github.com/MrNtlu/MyAnimeInfo/blob/master/app/src/main/java/com/mrntlu/myanimeinfo/service/repository/AnimeAPI.java

    @GET("anime/{mal_id}/{page}")
    suspend fun getAnimeByID(@Path("mal_id") mal_id:Int,@Path("page") page:Int):AnimeResponse

    @GET("anime/{mal_id}/characters_staff")
    suspend fun getCharacterByID(@Path("mal_id") mal_id: Int):CharactersResponse

    @GET("anime/{mal_id}/reviews/{page}")
    suspend fun getAnimeReviewsByID(@Path("mal_id") mal_id:Int,@Path("page") page:Int):ReviewsResponse

    @GET("top/anime/{page}/{subtype}")
    suspend fun getTopAnimes(@Path("page") page:Int,@Path("subtype") subtype:String):TopAnimeResponse

    @GET("character/{character_id}")
    suspend fun getCharacterInfoByID(@Path("character_id") character_id:Int):CharactersResponse

    @GET("schedule")
    suspend fun getAnimeSchedule():AnimeScheduleResponse
}