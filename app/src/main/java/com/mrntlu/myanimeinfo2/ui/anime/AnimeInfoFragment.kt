package com.mrntlu.myanimeinfo2.ui.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.InfoPagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class AnimeInfoFragment : Fragment(), CoroutinesErrorHandler {

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

        posterImage.setOnClickListener {
            val bundle= bundleOf("mal_id" to malID, "data_type" to DataType.ANIME.code)
            navController.navigate(R.id.action_animeInfo_to_pictures,bundle)
        }
    }

    private fun setupObservers() = animeViewModel.getAnimeByID(malID,this).observe(viewLifecycleOwner, Observer {
        animeResponse=it
        setupViewPagers(animeResponse)
        setupUI(animeResponse)
    })

    private fun setupUI(animeResponse: AnimeResponse) {
        progressbarLayout.setGone()
        posterImage.loadWithGlide(animeResponse.image_url,posterImageProgress)
        clickMoreText.setVisible()

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

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            progressbarLayout.setGone()
            errorLayout.setVisible()
            errorLayout.errorText.text=message
        }
    }

    override fun onDestroyView() {
        infoViewPager.adapter=null
        super.onDestroyView()
    }
}
