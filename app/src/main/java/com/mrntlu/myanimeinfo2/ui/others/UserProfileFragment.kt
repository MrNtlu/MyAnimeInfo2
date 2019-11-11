package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.UserProfilePagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.UserFavsResponse
import com.mrntlu.myanimeinfo2.models.UserProfileResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var username:String
    private lateinit var navController: NavController
    private lateinit var commonViewModel: CommonViewModel

    private lateinit var userProfileResponse: UserProfileResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username=it.getString("username","mrntlu")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        commonViewModel=ViewModelProviders.of(this).get(CommonViewModel::class.java)
        progressbarLayout.setVisible()

        setListeners()
        setupObserver()
    }

    private fun setListeners() {
        userAnimeListButton.setOnClickListener {
            val bundle= bundleOf("username" to userProfileResponse.username, "data_type" to DataType.ANIME.code)
            navController.navigate(R.id.action_userProfile_to_userList,bundle)
        }

        userMangaListButton.setOnClickListener {
            val bundle= bundleOf("username" to userProfileResponse.username, "data_type" to DataType.MANGA.code)
            navController.navigate(R.id.action_userProfile_to_userList,bundle)
        }

        errorLayout.errorRefreshButton.setOnClickListener {
            progressbarLayout.setVisible()
            errorLayout.setGone()
            setupObserver()
        }
    }

    private fun setUI(){
        userProfileImage.loadWithGlide(userProfileResponse.image_url,userProfileProgress)
        userProfileName.text=userProfileResponse.username
        progressbarLayout.setGone()
    }

    private fun setViewPager(userProfileResponse: UserProfileResponse) {
        val pagerAdapter=UserProfilePagerAdapter(
            childFragmentManager,
            userProfileResponse
        )
        profileViewPager.adapter=pagerAdapter
        profileTablayout.setupWithViewPager(profileViewPager)
        profileTablayout.getTabAt(0)?.setIcon(R.drawable.ic_growth)
    }

    private fun setupObserver() {
        commonViewModel.getUserProfile(username,this).observe(viewLifecycleOwner, Observer {
            userProfileResponse=it
            setUI()
            setViewPager(it)
        })
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main){
            progressbarLayout.setGone()
            errorLayout.setVisible()
            errorLayout.errorText.text=message
        }
    }

    override fun onDestroyView() {
        profileViewPager.adapter=null
        super.onDestroyView()
    }
}
