package com.mrntlu.myanimeinfo2.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainMangaFragment : Fragment() {

    private lateinit var viewModel: MangaViewModel
    private lateinit var topAiringAdapter: PreviewAnimeListAdapter
    private lateinit var topUpcomingAdapter: PreviewAnimeListAdapter
    private lateinit var todayAiringAdapter: PreviewAnimeListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(MangaViewModel::class.java)

        setupObservers()
        setupRecyclerView()
    }

    private fun setupObservers() {
        
    }

    private fun setupRecyclerView() {
        topAiringRV.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
        topUpcomingRV.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
        airingTodayRV.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
    }
}
