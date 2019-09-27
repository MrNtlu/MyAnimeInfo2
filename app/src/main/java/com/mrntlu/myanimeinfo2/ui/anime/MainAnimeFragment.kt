package com.mrntlu.myanimeinfo2.ui.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainAnimeFragment : Fragment(){

    private lateinit var viewModel: AnimeViewModel
    private lateinit var topAiringAdapter:PreviewAnimeListAdapter
    private lateinit var topUpcomingAdapter:PreviewAnimeListAdapter
    private lateinit var todayAiringAdapter:PreviewAnimeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(AnimeViewModel::class.java)

        setupObservers()
        setupRecyclerView()
    }

    private fun setupObservers() {
        viewModel.getTopAnimes(1,resources.getStringArray(R.array.topAnimeSubtypes)[1].toLowerCase(Locale.ENGLISH)).observe(viewLifecycleOwner,Observer{
            topAiringAdapter.submitList(it.top)
        })

        viewModel.getTopAnimes(1,resources.getStringArray(R.array.topAnimeSubtypes)[2].toLowerCase(Locale.ENGLISH)).observe(viewLifecycleOwner, Observer {
            topUpcomingAdapter.submitList(it.top)
        })

        viewModel.getAnimeSchedule().observe(viewLifecycleOwner, Observer {
            val scheduleList=when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
                Calendar.MONDAY->it.monday
                Calendar.TUESDAY->it.tuesday
                Calendar.WEDNESDAY->it.wednesday
                Calendar.THURSDAY->it.thursday
                Calendar.FRIDAY->it.friday
                Calendar.SATURDAY->it.saturday
                Calendar.SUNDAY->it.sunday
                else -> it.monday
            }
            todayAiringAdapter.submitList(scheduleList)

        })
    }

    private fun setupRecyclerView() {
        topAiringRV.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            topAiringAdapter=PreviewAnimeListAdapter(object : PreviewAnimeListAdapter.Interaction {
                override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                    printLog(message = "$position $item")
                }
            })
            adapter=topAiringAdapter
        }
        topUpcomingRV.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            topUpcomingAdapter=PreviewAnimeListAdapter(object : PreviewAnimeListAdapter.Interaction {
                override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                    printLog(message = "$position $item")
                }
            })
            adapter=topUpcomingAdapter
        }
        airingTodayRV.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            todayAiringAdapter=PreviewAnimeListAdapter(object : PreviewAnimeListAdapter.Interaction {
                override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                    printLog(message = "$position $item")
                }
            })
            adapter=todayAiringAdapter
        }
    }
}
