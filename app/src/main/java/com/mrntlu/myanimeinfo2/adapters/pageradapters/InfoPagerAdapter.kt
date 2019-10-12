package com.mrntlu.myanimeinfo2.adapters.pageradapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.ANIME
import com.mrntlu.myanimeinfo2.models.DataType.MANGA
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import com.mrntlu.myanimeinfo2.models.MangaResponse
import com.mrntlu.myanimeinfo2.ui.anime.AnimeProducersFragment
import com.mrntlu.myanimeinfo2.ui.common.*
import com.mrntlu.myanimeinfo2.utils.printLog

class InfoPagerAdapter(fm: FragmentManager, private val dataType: DataType, animeResponse: AnimeResponse?=null, mangaResponse: MangaResponse?=null): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private lateinit var animeFragmentList:List<Fragment>

    private lateinit var mangaFragmentList:List<Fragment>

    init {
        if (dataType==ANIME){
            animeFragmentList=listOf(
                DetailsFragment(animeResponse,dataType = ANIME),
                CharactersFragment(animeResponse!!.mal_id, ANIME),
                RelatedFragment(animeResponse.related,ANIME),
                RecommendationFragment(ANIME,animeResponse.mal_id),
                AnimeProducersFragment(animeResponse.producers ?: listOf()),
                ReviewsFragment(animeResponse.mal_id, ANIME)
            )
        }else{
            mangaFragmentList= listOf(
                DetailsFragment(mangaResponse = mangaResponse, dataType = MANGA),
                CharactersFragment(mangaResponse!!.mal_id, MANGA),
                RelatedFragment(mangaResponse.related,MANGA),
                RecommendationFragment(MANGA,mangaResponse.mal_id),
                ReviewsFragment(mangaResponse.mal_id, MANGA)
            )
        }
    }

    override fun getItem(position: Int)= if (dataType== ANIME) animeFragmentList[position] else mangaFragmentList[position]

    override fun getCount()=if (dataType== ANIME) animeFragmentList.size else mangaFragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        if (dataType==ANIME) {
            return when (position) {
                0 -> "Details"
                1 -> "Characters"
                2 -> "Related"
                3 -> "Recommendations"
                4 -> "Production"
                else -> "Reviews"
            }
        }else{
            return when(position){
                0 -> "Details"
                1 -> "Characters"
                2 -> "Related"
                3 -> "Recommendations"
                else -> "Reviews"
            }
        }
    }
}