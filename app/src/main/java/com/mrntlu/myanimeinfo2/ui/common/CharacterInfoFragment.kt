package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.CharacterInfoPagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.CharacterInfoResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_info.*
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class CharacterInfoFragment : Fragment(), CoroutinesErrorHandler{

    private lateinit var commonViewModel: CommonViewModel
    private var malID by Delegates.notNull<Int>()
    private lateinit var navController: NavController
    private lateinit var characterInfoResponse: CharacterInfoResponse

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
        commonViewModel=ViewModelProvider(this).get(CommonViewModel::class.java)
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

    private fun setupObservers() {
        commonViewModel.getCharacterInfoByID(malID,this).observe(viewLifecycleOwner, Observer {
            characterInfoResponse=it
            setupUI()
            setupViewPagers()
        })
    }

    private fun setupUI(){
        progressbarLayout.setGone()
        posterImage.loadWithGlide(characterInfoResponse.image_url,posterImageProgress)

        titleText.text=characterInfoResponse.name
        val type="Favs: ${characterInfoResponse.member_favorites}"
        typeText.text=type
    }

    private fun setupViewPagers(){
        val pagerAdapter=CharacterInfoPagerAdapter(childFragmentManager,characterInfoResponse)
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
