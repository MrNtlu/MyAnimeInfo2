package com.mrntlu.myanimeinfo2.adapters.pageradapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import com.mrntlu.myanimeinfo2.ui.anime.AnimeProducersFragment
import com.mrntlu.myanimeinfo2.ui.common.*
import com.mrntlu.myanimeinfo2.utils.printLog

class AnimeInfoPagerAdapter(fm: FragmentManager,animeResponse: AnimeResponse): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    //Todo consider making it common and get type
    private val fragmentList= listOf(
        DetailsFragment(animeResponse),
        CharactersFragment(animeResponse.mal_id,DataType.ANIME),
        RelatedFragment(animeResponse.related),
        RecommendationFragment(DataType.ANIME,animeResponse.mal_id),
        AnimeProducersFragment(animeResponse.producers ?: listOf()),
        ReviewsFragment(animeResponse.mal_id,DataType.ANIME)
    )

    override fun getItem(position: Int)= fragmentList[position]

    override fun getCount()=fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> "Details"
            1-> "Characters"
            2-> "Related"
            3-> "Recommendations"
            4-> "Production"
            else-> "Reviews"
        }
    }
}