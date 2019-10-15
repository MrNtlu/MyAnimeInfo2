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
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.utils.setGone
import kotlinx.android.synthetic.main.fragment_details.*
import javax.xml.parsers.FactoryConfigurationError

class DetailsFragment(private val animeResponse: AnimeResponse?=null,private val mangaResponse: MangaResponse?=null,private val dataType: DataType) : Fragment() {

    private lateinit var genreTagListAdapter: GenreTagListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupUI()
    }

    private fun setupUI() {
        animeResponse?.let {
            val rank="#${it.rank}"
            val popularity="#${it.popularity}"
            val members="#${it.members}"
            setData(it.score,it.scored_by,rank,popularity,members,it.premiered,it.duration,it.episodes,it.status,it.synopsis,it.background,it.broadcast,it.genres)
        }
        mangaResponse?.let {
            durationTextview.text="Volumes"
            episodesTextview.text="Chapters"
            val rank="#${it.rank}"
            val popularity="#${it.popularity}"
            val members="#${it.members}"
            val volumes:String= it.volumes?.toString() ?: "?"
            setData(it.score,it.scored_by,rank,popularity,members,null,volumes,it.chapters,it.status,it.synopsis,it.background,null,it.genres)
        }
    }

    private fun setData(score:Double,scoredBy:Int,rank:String,popularity:String,members:String,premiered:String?,duration:String?,episodes:Int?,status:String,
                        synopsis:String,background:String?,broadcast:String?,genres:List<GeneralShortResponse>?){
        scoreText.text=score.toString()
        scoredByText.text=scoredBy.toString()
        popularityText.text=popularity
        rankedText.text=rank
        membersText.text=members
        durationText.text= duration ?: "?"
        episodesText.text= episodes?.toString() ?: "?"
        statusText.text=status
        synopsisText.text=synopsis

        if (premiered==null) {
            premieredTextview.setGone()
            premieredText.setGone()
        }else premieredText.text=premiered

        if (background==null) backgroundCardView.setGone() else backgroundText.text=background

        if (broadcast==null){
            broadcastTextview.setGone()
            broadcastText.setGone()
        }else broadcastText.text=broadcast

        genres?.let {
            setupRecyclerView(it)
        }
    }

    private fun setupRecyclerView(list: List<GeneralShortResponse>) =genreListRV.apply {
        val linearLayoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        layoutManager=linearLayoutManager
        genreTagListAdapter=GenreTagListAdapter(object :GenreTagListAdapter.Interaction{
            override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                if (dataType==DataType.ANIME) {
                    val bundle = bundleOf(
                        "genre_name" to item.name,
                        "data_type" to DataType.ANIME.code,
                        "dialog_type" to DialogType.GENRE.code,
                        "mal_id" to item.mal_id)
                    navController.navigate(R.id.action_animeInfo_to_genreDialog, bundle)
                }else{
                    val bundle = bundleOf(
                        "genre_name" to item.name,
                        "data_type" to DataType.MANGA.code,
                        "dialog_type" to DialogType.GENRE.code,
                        "mal_id" to item.mal_id)
                    navController.navigate(R.id.action_mangaInfo_to_genreDialog, bundle)
                }
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
