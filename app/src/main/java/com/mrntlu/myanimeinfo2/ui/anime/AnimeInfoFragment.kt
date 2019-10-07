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
import com.mrntlu.myanimeinfo2.adapters.pageradapters.InfoPagerAdapter
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.fragment_info.*
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
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(AnimeViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() = animeViewModel.getAnimeByID(malID).observe(viewLifecycleOwner, Observer {
        animeResponse=it
        setupViewPagers(animeResponse)
        setupUI(animeResponse)
    })

    private fun setupUI(animeResponse: AnimeResponse) {
        posterImage.loadWithGlide(animeResponse.image_url,posterImageProgress)

        titleText.text=animeResponse.title
        typeText.text=animeResponse.type
    }

    private fun setupViewPagers(animeResponse: AnimeResponse) {
        val pagerAdapter= InfoPagerAdapter(
            childFragmentManager,
            animeResponse = animeResponse,
            dataType = DataType.ANIME
        )
        infoViewPager.adapter=pagerAdapter
        infoTabLayout.setupWithViewPager(infoViewPager)
    }

    override fun onDestroyView() {
        infoViewPager.adapter=null
        super.onDestroyView()
    }
}
