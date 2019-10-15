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
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.AnimeScheduleResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.android.synthetic.main.fragment_schedule_anime.*
import kotlinx.android.synthetic.main.fragment_schedule_anime.errorLayout
import kotlinx.android.synthetic.main.fragment_schedule_anime.progressbarLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class ScheduleAnimeFragment : Fragment(),CoroutinesErrorHandler {

    private lateinit var navController: NavController
    private lateinit var animeViewModel: AnimeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_schedule_anime, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
        progressbarLayout.setVisible()

        setListeners()
        setupObservers()
    }

    private fun setListeners() {
        errorLayout.errorRefreshButton.setOnClickListener {
            progressbarLayout.setVisible()
            errorLayout.setGone()
            setupObservers()
        }
    }

    private fun setupObservers() {
        animeViewModel.getAnimeSchedule(this).observe(viewLifecycleOwner, Observer {
            setupViewPagers(it)
        })
    }

    private fun setupViewPagers(animeScheduleResponse: AnimeScheduleResponse){
        progressbarLayout.setGone()
        val pagerAdapter= AnimeSchedulePagerAdapter(
            parentFragmentManager,
            animeScheduleResponse
        )
        scheduleViewPager.adapter=pagerAdapter
        scheduleTabLayout.setupWithViewPager(scheduleViewPager)

        val position=when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY-> 0
            Calendar.TUESDAY-> 1
            Calendar.WEDNESDAY-> 2
            Calendar.THURSDAY-> 3
            Calendar.FRIDAY-> 4
            Calendar.SATURDAY->5
            Calendar.SUNDAY-> 6
            else -> 0
        }
        val tab=scheduleTabLayout.getTabAt(position)
        tab?.select()
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            progressbarLayout.setGone()
            errorLayout.setVisible()
            errorLayout.errorText.text=message
        }
    }

    override fun onDestroyView() {
        scheduleViewPager.adapter=null
        super.onDestroyView()
    }
}
