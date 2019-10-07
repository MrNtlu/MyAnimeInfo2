package com.mrntlu.myanimeinfo2.adapters.pageradapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrntlu.myanimeinfo2.models.CharacterInfoResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.ui.common.CharacterAboutFragment
import com.mrntlu.myanimeinfo2.ui.common.CharacterDataographyFragment

class CharacterInfoPagerAdapter(fm:FragmentManager,characterInfoResponse: CharacterInfoResponse): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList= listOf(
        CharacterAboutFragment(characterInfoResponse.about),
        CharacterDataographyFragment(characterInfoResponse.animeography,DataType.ANIME),
        CharacterDataographyFragment(characterInfoResponse.mangaography,DataType.MANGA)
    )

    override fun getItem(position: Int)=fragmentList[position]

    override fun getCount()=fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0->"Details"
            1->"Animeography"
            else->"Mangaography"
        }
    }
}