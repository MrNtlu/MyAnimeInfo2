package com.mrntlu.myanimeinfo2.ui.others

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
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.UserProfileResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
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

        setListeners()
        setupRecyclerView()
        setViewPager()
        setupObserver()
    }

    private fun setListeners() {
        userAnimeListButton.setOnClickListener {

        }

        userMangaListButton.setOnClickListener {

        }
    }

    private fun setUI(){
        userProfileImage.loadWithGlide(userProfileResponse.image_url,userProfileProgress)
        userProfileName.text=userProfileResponse.username
    }

    private fun setViewPager() {

    }

    private fun setupRecyclerView() {
    }

    private fun setupObserver() {
        commonViewModel.getUserProfile(username,this).observe(viewLifecycleOwner, Observer {
            userProfileResponse=it
            printLog(message = it.toString())
            setUI()
        })
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main){
            printLog(message= message)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
