package com.mrntlu.myanimeinfo2.adapters.pageradapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class PicturesPagerAdapter(fm: FragmentManager, private val fragments:List<Fragment>): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int)=fragments[position]

    override fun getCount()=fragments.size
}