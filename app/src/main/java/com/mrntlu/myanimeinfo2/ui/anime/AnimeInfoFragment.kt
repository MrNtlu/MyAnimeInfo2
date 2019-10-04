package com.mrntlu.myanimeinfo2.ui.anime

import android.graphics.drawable.Drawable
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.AnimeInfoPagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.fragment_anime_info.*
import kotlin.properties.Delegates

class AnimeInfoFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var animeViewModel: AnimeViewModel
    private var malID by Delegates.notNull<Int>()

    private lateinit var animeResponse:AnimeResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            malID=it.getInt("mal_id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_anime_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(AnimeViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() = animeViewModel.getAnimeByID(malID).observe(viewLifecycleOwner, Observer {
        printLog(message = it.toString())
        animeResponse=it
        setupViewPagers(animeResponse)
        setupUI(animeResponse)
    })

    private fun setupUI(animeResponse: AnimeResponse) {
        context?.let {
            Glide.with(it).load(animeResponse.image_url).addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    posterImage.setImageResource(R.drawable.ic_no_picture)
                    posterImageProgress.setGone()
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    posterImageProgress.setGone()
                    return false
                }

            }).into(posterImage)
        }

        titleText.text=animeResponse.title
        typeText.text=animeResponse.type
    }

    private fun setupViewPagers(animeResponse: AnimeResponse) {
        val pagerAdapter= AnimeInfoPagerAdapter(
            childFragmentManager,
            animeResponse
        )
        infoViewPager.adapter=pagerAdapter
        infoTabLayout.setupWithViewPager(infoViewPager)
    }

    override fun onDestroyView() {
        infoViewPager.adapter=null
        super.onDestroyView()
    }
}
