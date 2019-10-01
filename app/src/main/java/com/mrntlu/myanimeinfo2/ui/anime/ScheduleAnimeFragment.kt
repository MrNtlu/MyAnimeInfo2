package com.mrntlu.myanimeinfo2.ui.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.AnimeSchedulePagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeScheduleResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.fragment_schedule_anime.*

class ScheduleAnimeFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var animeViewModel: AnimeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_anime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(AnimeViewModel::class.java)

        scheduleProgressBar.setVisible()
        setupObservers()
    }

    private fun setupObservers() {
        animeViewModel.getAnimeSchedule().observe(viewLifecycleOwner, Observer {
            scheduleProgressBar.setGone()
            setupViewPagers(it)
        })
    }

    private fun setupViewPagers(animeScheduleResponse: AnimeScheduleResponse){
        val pagerAdapter= AnimeSchedulePagerAdapter(
            childFragmentManager,
            animeScheduleResponse
        )
        scheduleViewPager.adapter=pagerAdapter
        scheduleTabLayout.setupWithViewPager(scheduleViewPager)
        scheduleProgressBar.setGone()
    }
}
