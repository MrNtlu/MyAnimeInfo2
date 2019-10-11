package com.mrntlu.myanimeinfo2.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
        mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
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
    }

    private fun setupObservers()=mangaViewModel.getMangaByID(malID,this).observe(viewLifecycleOwner, Observer {
        mangaResponse=it
        setupViewPagers()
        setupUI()
    })

    private fun setupUI(){
        progressbarLayout.setGone()
        posterImage.loadWithGlide(mangaResponse.image_url,posterImageProgress)

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
