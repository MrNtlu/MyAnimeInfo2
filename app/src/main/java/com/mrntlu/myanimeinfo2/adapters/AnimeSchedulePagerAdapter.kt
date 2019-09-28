package com.mrntlu.myanimeinfo2.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeScheduleResponse
import com.mrntlu.myanimeinfo2.ui.anime.ScheduleInfoFragment

class AnimeSchedulePagerAdapter(fm:FragmentManager,animeScheduleResponse: AnimeScheduleResponse):FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragmentList= listOf(
        ScheduleInfoFragment(animeScheduleResponse.monday),
        ScheduleInfoFragment(animeScheduleResponse.tuesday),
        ScheduleInfoFragment(animeScheduleResponse.wednesday),
        ScheduleInfoFragment(animeScheduleResponse.thursday),
        ScheduleInfoFragment(animeScheduleResponse.friday),
        ScheduleInfoFragment(animeScheduleResponse.saturday),
        ScheduleInfoFragment(animeScheduleResponse.sunday)
    )

    override fun getItem(position: Int)= fragmentList[position]

    override fun getCount()=7

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0-> "Monday"
            1-> "Tuesday"
            2-> "Wednesday"
            3-> "Thursday"
            4-> "Friday"
            5-> "Saturday"
            else-> "Sunday"
        }
    }
}