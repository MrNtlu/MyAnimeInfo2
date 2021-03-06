package com.mrntlu.myanimeinfo2.models

import com.google.gson.annotations.SerializedName

data class ProducerInfoResponse(val anime:List<PreviewAnimeResponse>)

data class GeneralShortResponse(val mal_id:Int,val type:String,val name:String)

data class CharactersResponse(val characters:List<CharacterBodyResponse>)

data class CharacterBodyResponse(val mal_id: Int,val name: String,val image_url: String,val role:String)

data class CharacterInfoResponse(val mal_id: Int,val name: String,val image_url: String,val about:String,val member_favorites:Int,val animeography:List<CharacterBodyResponse>,val mangaography:List<CharacterBodyResponse>)

data class RecommendationsResponse(val recommendations:List<RecommendationsBodyResponse>)

data class RecommendationsBodyResponse(val mal_id: Int,val image_url: String,val title: String,val recommendation_count:Int)

data class ReviewsResponse(val reviews:List<ReviewsBodyResponse>)

data class ReviewsBodyResponse(val mal_id: Int,val content:String,val helpful_count:Int,val reviewer:ReviewerResponse)

data class ReviewerResponse(val username:String,val scores:ScoresResponse,val image_url: String)

data class ScoresResponse(val overall:Int,val story:Int,val animation:Int,val sound:Int,val character:Int,val enjoyment:Int)

data class PicturesResponse(val pictures:List<PictureBodyResponse>)

data class PictureBodyResponse(val large:String,val small:String)

data class RelatedResponse(val Adaptation:List<GeneralShortResponse>?, @SerializedName(value = "Side story") val Side:List<GeneralShortResponse>?,
                           val Prequel:List<GeneralShortResponse>?, val Sequel:List<GeneralShortResponse>?)

data class PreviewAnimeResponse(val mal_id:Int,val type:String,val image_url:String,val episodes:Int?,
                                val score:Double,val title:String)

data class PreviewMangaResponse(val mal_id:Int,val type:String,val image_url:String,val volumes:Int?,
                                val chapters:Int?,val score:Double,val title:String)

data class TopAnimeResponse(val top:List<PreviewAnimeResponse>)

data class TopMangaResponse(val top:List<PreviewMangaResponse>)

data class AnimeResponse(val mal_id:Int, val image_url: String, val title:String, val type:String, val episodes:Int?,
                         val status:String, val airing:Boolean, val duration:String?, val score:Double, val scored_by:Int,
                         val rank: Int, val popularity:Int, val members: Int, val favorites:Int, val synopsis:String, val background:String?,
                         val premiered:String?, val broadcast:String?, val related:RelatedResponse, val producers:List<GeneralShortResponse>?,
                         val studios:List<GeneralShortResponse>?, val genres:List<GeneralShortResponse>?)

data class MangaResponse(val mal_id:Int, val image_url: String, val title:String, val type:String, val volumes:Int?, val chapters:Int?,
                         val status:String, val publishing:Boolean, val score:Double, val scored_by:Int, val rank: Int, val popularity:Int,
                         val members: Int, val favorites:Int, val synopsis:String, val background:String?, val related:RelatedResponse,
                         val authors:List<GeneralShortResponse>?, val genres:List<GeneralShortResponse>?)

data class AnimeSearchResponse(val results:List<PreviewAnimeResponse>)

data class MangaSearchResponse(val results:List<PreviewMangaResponse>)

data class AnimeScheduleResponse(val monday:List<PreviewAnimeResponse>,val tuesday:List<PreviewAnimeResponse>,val wednesday:List<PreviewAnimeResponse>,val thursday:List<PreviewAnimeResponse>,
                                 val friday:List<PreviewAnimeResponse>,val saturday:List<PreviewAnimeResponse>,val sunday:List<PreviewAnimeResponse>)

data class AnimeGenreSeasonResponse(val mal_url:GeneralShortResponse,val anime:List<PreviewAnimeResponse>)

data class MangaGenreSeasonResponse(val mal_url:GeneralShortResponse,val manga:List<PreviewMangaResponse>)

//User
data class UserProfileResponse(val username: String, val image_url: String?, val anime_stats:UserAnimeStats, val manga_stats:UserMangaStats,val favorites:UserFavsResponse)

data class UserFavsResponse(val anime:List<UserShortResponse>, val manga:List<UserShortResponse>, val characters:List<UserShortResponse>, val people:List<UserShortResponse>)

data class UserShortResponse(val mal_id: Int,val image_url: String,val name: String)

data class UserAnimeStats(val days_watched: Double, val mean_score: Double, val watching: Int, val completed: Int,
                          val on_hold: Int, val dropped: Int, val plan_to_watch: Int,val rewatched:Int,val episodes_watched: Int)

data class UserMangaStats(val days_read: Double, val mean_score: Double, val reading: Int, val completed: Int,
                          val on_hold: Int, val dropped: Int, val plan_to_read: Int,val reread:Int,val chapters_read: Int,val volumes_read: Int)

data class UserAnimeListResponse(val anime:List<UserAnimeListBody>)

data class UserMangaListResponse(val manga:List<UserMangaListBody>)

data class UserAnimeListBody(val mal_id: Int,val title: String,val image_url: String,val type: String,val watching_status:Int,val score: Double,val watched_episodes:Int,val total_episodes:Int)

data class UserMangaListBody(val mal_id: Int,val title: String,val image_url: String,val type: String,val reading_status:Int,val score: Double,val read_chapters:Int,
                             val read_volumes:Int,val total_chapters:Int,val total_volumes:Int)

