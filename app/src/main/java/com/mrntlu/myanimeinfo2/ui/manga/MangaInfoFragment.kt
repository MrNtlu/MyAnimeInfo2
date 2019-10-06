package com.mrntlu.myanimeinfo2.ui.manga

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
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.MangaResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_anime_info.*
import kotlin.properties.Delegates

class MangaInfoFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mangaViewModel: MangaViewModel
    private var malID by Delegates.notNull<Int>()

    private lateinit var mangaResponse:MangaResponse

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
        mangaViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(MangaViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers()=mangaViewModel.getMangaByID(malID).observe(viewLifecycleOwner, Observer {
        mangaResponse=it
        setupViewPagers(mangaResponse)
        setupUI(mangaResponse)
    })

    private fun setupUI(mangaResponse: MangaResponse){
        posterImage.loadWithGlide(mangaResponse.image_url,posterImageProgress)

        titleText.text=mangaResponse.title
        typeText.text=mangaResponse.type
    }

    private fun setupViewPagers(mangaResponse: MangaResponse){
        val pagerAdapter= InfoPagerAdapter(
            childFragmentManager,
            mangaResponse= mangaResponse,
            dataType = DataType.MANGA
        )
        infoViewPager.adapter=pagerAdapter
        infoTabLayout.setupWithViewPager(infoViewPager)
    }

    override fun onDestroyView() {
        infoViewPager.adapter=null
        super.onDestroyView()
    }
}
