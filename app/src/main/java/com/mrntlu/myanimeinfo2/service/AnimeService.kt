package com.mrntlu.myanimeinfo2.service

import com.mrntlu.myanimeinfo2.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeService {
    //https://github.com/MrNtlu/MyAnimeInfo/blob/master/app/src/main/java/com/mrntlu/myanimeinfo/service/repository/AnimeAPI.java
    //Anime

    @GET("anime/{mal_id}")
    suspend fun getAnimeByID(@Path("mal_id") mal_id:Int):AnimeResponse

    @GET("anime/{mal_id}/characters_staff")
    suspend fun getAnimeCharactersByID(@Path("mal_id") mal_id: Int):CharactersResponse

    @GET("top/anime/{page}/{subtype}")
    suspend fun getTopAnimes(@Path("page") page:Int,@Path("subtype") subtype:String):TopAnimeResponse

    @GET("schedule")
    suspend fun getAnimeSchedule():AnimeScheduleResponse

    @GET("genre/anime/{genreID}/{page}")
    suspend fun getAnimeByGenre(@Path("genreID") genreID:Int,@Path("page") page:Int):AnimeGenreSeasonResponse

    @GET("season/{year}/{season}")
    suspend fun getAnimeBySeason(@Path("year") year:String,@Path("season")season:String):AnimeGenreSeasonResponse

    @GET("search/anime")
    suspend fun getAnimeBySearch(@Query("q") q:String, @Query("page") page:Int):AnimeSearchResponse

    //Manga
    @GET("manga/{mal_id}")
    suspend fun getMangaByID(@Path("mal_id") mal_id:Int):MangaResponse

    @GET("manga/{mal_id}/reviews/{page}")
    suspend fun getMangaReviewsByID(@Path("mal_id") mal_id:Int,@Path("page") page:Int):ReviewsResponse

    @GET("top/manga/{page}/{subtype}")
    suspend fun getTopMangas(@Path("page") page:Int,@Path("subtype") subtype:String):TopMangaResponse

    @GET("manga/{mal_id}/characters")
    suspend fun getMangaCharactersByID(@Path("mal_id") mal_id: Int):CharactersResponse

    @GET("genre/manga/{genreID}/{page}")
    suspend fun getMangaByGenre(@Path("genreID") genreID:Int,@Path("page") page:Int):MangaGenreSeasonResponse

    @GET("search/manga")
    suspend fun getMangaBySearch(@Query("q") q:String, @Query("page") page:Int):MangaSearchResponse

    //Common
    @GET("producer/{mal_id}")
    suspend fun getProducerInfoByID(@Path("mal_id") mal_id:Int):ProducerInfoResponse

    @GET("{type}/{mal_id}/recommendations")
    suspend fun getRecommendationsByID(@Path("type") type:String,@Path("mal_id") mal_id:Int):RecommendationsResponse

    @GET("character/{character_id}")
    suspend fun getCharacterInfoByID(@Path("character_id") character_id:Int):CharacterInfoResponse

    @GET("{type}/{mal_id}/reviews/{page}")
    suspend fun getReviewsByID(@Path("type") type:String,@Path("mal_id") mal_id:Int,@Path("page") page:Int):ReviewsResponse

    @GET("{type}/{mal_id}/pictures")
    suspend fun getPicturesByID(@Path("type") type:String,@Path("mal_id") mal_id:Int):PicturesResponse
}