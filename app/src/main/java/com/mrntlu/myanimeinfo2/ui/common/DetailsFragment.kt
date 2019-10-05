package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.GenreTagListAdapter
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import kotlinx.android.synthetic.main.fragment_details.*
import javax.xml.parsers.FactoryConfigurationError

class DetailsFragment(private val animeResponse: AnimeResponse) : Fragment() {

    private lateinit var genreTagListAdapter: GenreTagListAdapter
    private lateinit var navController: NavController

    //TODO if anime animeResponse else mangaResponse
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupUI()
    }

    private fun setupUI() {
        animeResponse.let {
            scoreText.text=it.score.toString()
            scoredByText.text=it.scored_by.toString()
            val rank="#${it.rank}"
            val popularity="#${it.popularity}"
            val members="#${it.members}"
            popularityText.text=popularity
            rankedText.text=rank
            membersText.text=members

            premieredText.text=it.premiered
            durationText.text=if (it.duration==null) "?" else it.duration.toString()
            episodesText.text=if (it.episodes==null) "?" else it.episodes.toString()
            statusText.text=it.status
            synopsisText.text=it.synopsis
            if (it.background==null) backgroundCardView.setGone() else backgroundText.text=it.background
            if (it.broadcast==null){
                broadcastTextview.setGone()
                broadcastText.setGone()
            }else broadcastText.text=it.broadcast

            animeResponse.genres?.let { list ->
                setupRecyclerView(list)
            }
        }
    }

    private fun setupRecyclerView(list: List<GeneralShortResponse>) =genreListRV.apply {
        val linearLayoutManager=LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        layoutManager=linearLayoutManager
        genreTagListAdapter=GenreTagListAdapter(object :GenreTagListAdapter.Interaction{
            override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                val bundle= bundleOf("genre_name" to item.name,"data_type" to DataType.ANIME.code, "mal_id" to item.mal_id)
                navController.navigate(R.id.action_animeInfo_to_genreDialog,bundle)
            }
        })
        adapter=genreTagListAdapter
        genreTagListAdapter.submitList(list)
    }

    override fun onDestroyView() {
        genreListRV.adapter=null
        super.onDestroyView()
    }
}
