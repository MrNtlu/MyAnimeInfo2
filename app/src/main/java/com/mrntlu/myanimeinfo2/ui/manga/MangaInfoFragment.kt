package com.mrntlu.myanimeinfo2.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.InfoPagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.MangaResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MangaInfoFragment : Fragment(), CoroutinesErrorHandler {

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
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        mangaViewModel = ViewModelProvider(this).get(MangaViewModel::class.java)
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
            val bundle= bundleOf("mal_id" to malID, "data_type" to DataType.MANGA.code)
            navController.navigate(R.id.action_mangaInfo_to_pictures,bundle)
        }
    }

    private fun setupObservers()=mangaViewModel.getMangaByID(malID,this).observe(viewLifecycleOwner, Observer {
        mangaResponse=it
        setupViewPagers()
        setupUI()
    })

    private fun setupUI(){
        progressbarLayout.setGone()
        posterImage.loadWithGlide(mangaResponse.image_url,posterImageProgress)
        clickMoreText.setVisible()

        titleText.text=mangaResponse.title
        typeText.text=mangaResponse.type
    }

    private fun setupViewPagers(){
        val pagerAdapter= InfoPagerAdapter(
            childFragmentManager,
            mangaResponse= mangaResponse,
            dataType = DataType.MANGA
        )
        infoViewPager.adapter=pagerAdapter
        infoTabLayout.setupWithViewPager(infoViewPager)
    }

    override fun onError(message: String) {
        viewLifecycleOwner.lifecycleScope.launch{
            whenResumed {
                progressbarLayout.setGone()
                errorLayout.setVisible()
                errorLayout.errorText.text = message
            }
        }
    }

    override fun onDestroyView() {
        infoViewPager.adapter=null
        super.onDestroyView()
    }
}
