package com.mrntlu.myanimeinfo2.adapters.pageradapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.mrntlu.myanimeinfo2.models.UserFavsResponse
import com.mrntlu.myanimeinfo2.ui.others.ProfileFavoriteType
import com.mrntlu.myanimeinfo2.ui.others.UserProfileFavoritesFragment
import com.mrntlu.myanimeinfo2.ui.others.UserProfileStatsFragment

class UserProfilePagerAdapter(fm:FragmentManager,userFavsResponse: UserFavsResponse):FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList: List<Fragment> = listOf(
        UserProfileStatsFragment(),
        UserProfileFavoritesFragment(userFavsResponse.anime,ProfileFavoriteType.ANIME),
        UserProfileFavoritesFragment(userFavsResponse.manga,ProfileFavoriteType.MANGA),
        UserProfileFavoritesFragment(userFavsResponse.characters, ProfileFavoriteType.CHARACTERS),
        UserProfileFavoritesFragment(userFavsResponse.people,ProfileFavoriteType.PEOPLE)
    )

    override fun getItem(position: Int)=fragmentList[position]

    override fun getCount()=fragmentList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0->""
            1-> "Anime"
            2-> "Manga"
            3-> "Characters"
            else-> "People"
        }
    }
}