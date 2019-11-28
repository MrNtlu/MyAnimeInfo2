package com.mrntlu.myanimeinfo2.ui.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.models.UserProfileResponse
import com.mrntlu.myanimeinfo2.utils.*
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_no_user_header.view.*
import kotlinx.android.synthetic.main.nav_no_user_header.view.malUserText
import kotlinx.android.synthetic.main.nav_user_header.*
import kotlinx.android.synthetic.main.nav_user_header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHeader:View
    private lateinit var commonViewModel: CommonViewModel
    private var username:String?=null
    private var malUser:UserProfileResponse?=null

    private var isErrorOccurred=false
    private var errorMessage="Error Occurred!"

    override fun onBackPressed() {
        if (::navController.isInitialized && navController.currentDestination?.id==R.id.mainFragment){
            MaterialDialog(this).show {
                message(text = "Do you want to EXIT?")
                positiveButton(text = "Yes"){
                    finish()
                }
                negativeButton(text = "Stay"){dialog ->
                    dialog.dismiss()
                }
                cornerRadius(6f)
            }
        }else super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment=nav_host_fragment as NavHostFragment
        navController=navHostFragment.navController
        commonViewModel=ViewModelProviders.of(this).get(CommonViewModel::class.java)
        username=readFromPref()

        setListeners()
        setAds()

        setSupportActionBar(toolbar)
        setDrawer()
        setStatusBarColor()
        setNavBarColor()
    }

    private fun setListeners() {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            toolbar.title=when(destination.id){
                R.id.userSearchFragment->"Search User"
                R.id.userProfileFragment->{
                    if (arguments!=null){
                        val username=arguments.getString("username")
                        "$username's Profile"
                    }else "User's Profile"
                }
                R.id.animeSeasonFragment->"Anime by Season"
                R.id.mangaInfoFragment->"Manga Info"
                R.id.animeInfoFragment->"Anime Info"
                R.id.topListFragment->{
                    if (arguments!=null){
                        val dataType=DataType.getByCode(arguments.getInt("data_type"))
                        "Top ${dataType.name.toLowerCase(Locale.ENGLISH).makeCapital()} List"
                    } else "Top List"
                }
                R.id.scheduleAnimeFragment->"Schedule"
                R.id.genreDialogFragment->toolbar.title
                R.id.characterInfoFragment-> "Character Info"
                R.id.searchFragment->{
                    if (arguments!=null){
                        val dataType=DataType.getByCode(arguments.getInt("data_type"))
                        "Search ${dataType.name.toLowerCase(Locale.ENGLISH).makeCapital()}"
                    } else "Search"
                }
                R.id.picturesFragment->"Pictures"
                R.id.userListFragment->{
                    if (arguments!=null) {
                        val dataType = DataType.getByCode(arguments.getInt("data_type"))
                        "${dataType.name.toLowerCase(Locale.ENGLISH).makeCapital()} List"
                    }else "User List"
                }
                R.id.aboutMeFragment->"About Me"
                else->""
            }
            if (!adView.isVisible && isInternetAvailable(this)) setAds()
        }
    }

    private fun setAds(){
        adView.adListener=object :AdListener(){
            override fun onAdLoaded() {
                adView.setVisible()
                super.onAdLoaded()
            }

            override fun onAdFailedToLoad(p0: Int) {
                adView.setGone()
                super.onAdFailedToLoad(p0)
            }
        }

        MobileAds.initialize(this)
        val adRequest=AdRequest.Builder()
                //TODO remove
            .addTestDevice("AD4218F6AC5DB23A77A519172E0D2A6D")
            .addTestDevice("C15BA4A4DC246726736F6CAC69AAAE7E")
            .addTestDevice("544C0113FE3A3977A8B86DCBE3B024C0")
            .build()
        adView.loadAd(adRequest)
    }

    private fun setNavBarColor(){
        window.navigationBarColor=resources.getColor(R.color.black,theme)
    }

    private fun setStatusBarColor(){
        window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor=resources.getColor(R.color.white,theme)
    }

    private fun setDrawer(){
        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.main_page_menu->navController.navigate(R.id.action_global_main)
                R.id.anime_schedule->navController.navigate(R.id.action_global_scheduleAnime)
                R.id.top_anime_list->navController.navigate(R.id.action_global_topList,bundleOf("data_type" to ANIME.code))
                R.id.anime_by_season->navController.navigate(R.id.action_global_animeSeason)
                R.id.top_manga_list->navController.navigate(R.id.action_global_topList, bundleOf("data_type" to MANGA.code))
                R.id.anime_search->navController.navigate(R.id.action_global_search, bundleOf("data_type" to ANIME.code))
                R.id.manga_search->navController.navigate(R.id.action_global_search, bundleOf("data_type" to MANGA.code))
                R.id.search_user->navController.navigate(R.id.action_global_userSearch)
                R.id.about_me->navController.navigate(R.id.action_global_aboutMe)
            }

            drawerLayout.closeDrawer(START)
            true
        }
        setHeader()

        val toggle=ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.setToolbarNavigationClickListener {
            if (drawerLayout.isDrawerVisible(START)){
                drawerLayout.closeDrawer(START)
            }else{
                drawerLayout.openDrawer(START)
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setNavHeader(layout:Int){
        if(::navHeader.isInitialized) navigationView.removeHeaderView(navHeader)
        navHeader=navigationView.inflateHeaderView(layout)
    }

    private fun setHeader() {
        if (malUser!=null && username!=null){
            setNavHeader(R.layout.nav_user_header)
            navHeader.malUserText.text=malUser!!.username
            if(malUser!!.image_url != null) navHeader.malUserImage.loadWithGlide(malUser!!.image_url!!,navHeader.malUserProgress)
            else{
                malUserProgress.setGone()
                navHeader.malUserImage.setImageDrawable(resources.getDrawable(R.drawable.ic_person_black_24dp,theme))
            }

            setHeaderListeners(navHeader)
        }else if (malUser==null && username!=null){
            setMALUserData()
        }else{
            setNavHeader(R.layout.nav_no_user_header)
            navHeader.malUserAddButton.setOnClickListener {
                setUsernameInputDialog("Set MAL Username")
            }

            if (isErrorOccurred){
                navHeader.malUserText.text=errorMessage
                navHeader.malUserText.setTextColor(resources.getColor(R.color.red800,theme))
                navHeader.malUserInfoText.text=resources.getString(R.string.please_retry_again)
                isErrorOccurred=false
            }
        }

    }

    private fun setUsernameInputDialog(title:String){
        drawerLayout.closeDrawer(START)

        MaterialDialog(this).show {
            input { _, text ->
                username=text.toString()
                setMALUserData()
            }
            title(text = title)
            positiveButton(text = "Set")
            negativeButton(text = "Cancel")
            cornerRadius(6f)
        }
    }

    private fun setMALUserData(){
        setNavHeader(R.layout.nav_loading_user)

        commonViewModel.getUserProfile(username!!,object:CoroutinesErrorHandler{
            override fun onError(message: String) {
                GlobalScope.launch(Dispatchers.Main){
                    navHeaderErrorHandler(message)
                }
            }
        }).observe(this, Observer {
            malUser=it
            writeToPref(username!!)
            setHeader()
        })
    }

    private fun navHeaderErrorHandler(message:String) {
        username=null
        isErrorOccurred=true
        errorMessage=message
        setHeader()
    }

    private fun setHeaderListeners(navHeader: View) {
        navHeader.malUserProfileButton.setOnClickListener {
            val bundle = bundleOf("username" to malUser!!.username)
            navController.navigate(R.id.action_global_userProfile,bundle)
            drawerLayout.closeDrawer(START)
        }

        navHeader.malUserAnimeButton.setOnClickListener {
            val bundle= bundleOf("username" to malUser!!.username, "data_type" to ANIME.code)
            navController.navigate(R.id.action_global_userList,bundle)
            drawerLayout.closeDrawer(START)
        }

        navHeader.malUserMangaButton.setOnClickListener {
            val bundle= bundleOf("username" to malUser!!.username, "data_type" to MANGA.code)
            navController.navigate(R.id.action_global_userList,bundle)
            drawerLayout.closeDrawer(START)
        }

        navHeader.malUserChangeButton.setOnClickListener {
            setUsernameInputDialog("Change MAL Username")
        }

        navHeader.malUserRemoveButton.setOnClickListener {
            username=null
            malUser=null
            deletePref()
            setHeader()
        }
    }

    override fun onDestroy() {
        setSupportActionBar(null)
        adView.adListener=null
        super.onDestroy()
    }
}