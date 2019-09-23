package com.mrntlu.myanimeinfo2.models

data class GeneralShortResponse(val mal_id:Int,val type:String,val name:String)

data class PreviewAnimeResponse(val mal_id:Int,val type:String,val image_url:String,val episodes:Int,
                                val score:Double,val title:String)

data class PreviewMangaResponse(val mal_id:Int,val type:String,val image_url:String,val volumes:Int,
                                val score:Double,val title:String)

data class TopAnimeResponse(val mal_id:Int,val type:String,val image_url:String,val episodes:Int?,val score:Double,
                            val rank:Int,val title:String,val start_date:String?,val end_date:String?,val members:Int)

data class TopMangaResponse(val mal_id:Int,val type:String,val image_url:String,val volumes:Int?,val score:Double,
                            val rank:Int,val title:String,val start_date:String?,val end_date:String?,val members:Int)

data class AnimeResponse(val mal_id:Int,val image_url: String,val title:String,val type:String,val episodes:Int?,
                         val status:String,val airing:Boolean,val duration:String?,val score:Double,val scored_by:Int,
                         val rank: Int,val popularity:Int,val members: Int,val favorites:Int,val synopsis:String,val background:String?,
                         val premiered:String?,val broadcast:String?,val related:RelatedAnimesResponse,val producers:List<GeneralShortResponse>?,
                         val studios:List<GeneralShortResponse>?,val genres:List<GeneralShortResponse>?)

data class MangaResponse(val mal_id:Int,val image_url: String,val title:String,val type:String,val volumes:Int?,val chapters:Int?,
                         val status:String,val publishing:Boolean,val duration:String?,val score:Double,val scored_by:Int,
                         val rank: Int,val popularity:Int,val members: Int,val favorites:Int,val synopsis:String,val background:String?,
                         val related:RelatedAnimesResponse,val authors:List<GeneralShortResponse>?,val genres:List<GeneralShortResponse>?)

data class RelatedAnimesResponse(val Adaptation:List<GeneralShortResponse>?,val Side:List<GeneralShortResponse>?,
                                 val Other:List<GeneralShortResponse>?,val Prequel:List<GeneralShortResponse>?,val Sequel:List<GeneralShortResponse>?)

data class CharactersResponse(val characters:List<CharacterBodyResponse>)

data class CharacterBodyResponse(val mal_id: Int,val name: String,val image_url: String,val role:String)

data class RecommendationsResponse(val recommendations:List<RecommendationsBodyResponse>)

data class RecommendationsBodyResponse(val mal_id: Int,val image_url: String,val title: String,val recommendation_count:Int)

data class ReviewsResponse(val reviews:List<ReviewsBodyResponse>)

data class ReviewsBodyResponse(val mal_id: Int,val username:String,val scores:ScoresResponse,val content:String)

data class ScoresResponse(val overall:Int,val story:Int,val animation:Int,val sound:Int,val character:Int,val enjoyment:Int)

data class AnimeScheduleResponse(val monday:List<PreviewAnimeResponse>,val tuesday:List<PreviewAnimeResponse>,val wednesday:List<PreviewAnimeResponse>,val thursday:List<PreviewAnimeResponse>,
                                 val friday:List<PreviewAnimeResponse>,val saturday:List<PreviewAnimeResponse>,val sunday:List<PreviewAnimeResponse>)

data class AnimeGenreSeasonResponse(val anime:List<PreviewAnimeResponse>)