package com.mrntlu.myanimeinfo2.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.ui.common.CharactersFragment
import com.mrntlu.myanimeinfo2.ui.common.DetailsFragment

class AnimeInfoPagerAdapter(fm: FragmentManager,animeResponse: AnimeResponse): FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private val fragmentList= listOf(
        DetailsFragment(animeResponse),
        CharactersFragment(animeResponse.mal_id,1)
    )

    override fun getItem(position: Int)= fragmentList[position]

    override fun getCount()=2

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> "Details"
            /*1-> "Characters"
            2-> "Related"
            3-> "Recommendations"
            4-> "Production"*/
            else-> "Reviews"
        }
    }
}