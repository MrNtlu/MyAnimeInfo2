package com.mrntlu.myanimeinfo2.ui.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class ScheduleInfoFragment(private val scheduleAnimeList:List<PreviewAnimeResponse>): Fragment() {

    private lateinit var scheduleAdapter: PreviewAnimeListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView()=fragmentRV.apply {
        scheduleAdapter= PreviewAnimeListAdapter(R.layout.cell_preview_large,object : Interaction<PreviewAnimeResponse> {
            override fun onErrorRefreshPressed() {}

            override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_scheduleAnime_to_animeInfo, bundle)
            }
        })
        val gridLayoutManager= GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (scheduleAdapter.getItemViewType(position)==scheduleAdapter.ITEM_HOLDER) 1 else 2
            }
        }
        layoutManager=gridLayoutManager
        scheduleAdapter.submitList(scheduleAnimeList)
        adapter=scheduleAdapter
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
