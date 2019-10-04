package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment(private val animeResponse: AnimeResponse) : Fragment() {

    //TODO if anime animeResponse else mangaResponse
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        }
    }
}
