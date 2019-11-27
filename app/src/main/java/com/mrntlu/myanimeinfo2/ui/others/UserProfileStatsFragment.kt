package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.UserProfileResponse
import kotlinx.android.synthetic.main.fragment_user_profile_stats.*

class UserProfileStatsFragment(private val userProfileResponse: UserProfileResponse) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_profile_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMangaUI()
        setAnimeUI()
    }

    private fun setMangaUI() {
        val mangaStats=userProfileResponse.manga_stats

        mangaDaysText.text=mangaStats.days_read.toString()
        mangaMeanText.text=mangaStats.mean_score.toString()
        mangaReadingText.text=mangaStats.reading.toString()
        mangaCompletedText.text=mangaStats.completed.toString()
        mangaOnHoldText.text=mangaStats.on_hold.toString()
        mangaDroppedText.text=mangaStats.dropped.toString()
        mangaPlanText.text=mangaStats.plan_to_read.toString()
        mangaVolumesText.text=mangaStats.volumes_read.toString()
        mangaChaptersText.text=mangaStats.chapters_read.toString()
        mangaRereadText.text=mangaStats.reread.toString()
    }

    private fun setAnimeUI() {
        val animeStats=userProfileResponse.anime_stats

        animeDaysText.text=animeStats.days_watched.toString()
        animeMeanText.text=animeStats.mean_score.toString()
        animeWatchingText.text=animeStats.watching.toString()
        animeCompletedText.text=animeStats.completed.toString()
        animeOnHoldText.text=animeStats.on_hold.toString()
        animeDroppedText.text=animeStats.dropped.toString()
        animePlanText.text=animeStats.plan_to_watch.toString()
        animeEpisodesText.text=animeStats.episodes_watched.toString()
        animeRewatchedText.text=animeStats.rewatched.toString()
    }
}
