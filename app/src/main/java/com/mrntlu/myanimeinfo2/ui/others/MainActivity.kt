package com.mrntlu.myanimeinfo2.ui.others

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.core.view.GravityCompat.*
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
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
import kotlinx.android.synthetic.main.nav_user_header.view.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var navHeader:View
    private lateinit var commonViewModel: CommonViewModel
    private var malUser:UserProfileResponse?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment=nav_host_fragment as NavHostFragment
        navController=navHostFragment.navController
        commonViewModel=ViewModelProviders.of(this).get(CommonViewModel::class.java)

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
                else->""
            }
            if (!adView.isVisible && isInternetAvailable(this)) setAds()
        }
        setAds()

        setSupportActionBar(toolbar)
        setDrawer()
        setStatusBarColor(R.color.white,true)
        setNavBarColor(R.color.black)

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
            .addTestDevice("AD4218F6AC5DB23A77A519172E0D2A6D")
            .addTestDevice("544C0113FE3A3977A8B86DCBE3B024C0")
            .build()
        adView.loadAd(adRequest)
    }

    private fun setNavBarColor(color:Int){
        window.navigationBarColor=resources.getColor(color,theme)
    }

    fun setStatusBarColor(color:Int,isLight:Boolean){
        if (isLight) window.decorView.systemUiVisibility=View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor=resources.getColor(color,theme)
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

    private fun setHeader() {
        if (malUser!=null){
            if(::navHeader.isInitialized) navigationView.removeHeaderView(navHeader)
            navHeader=navigationView.inflateHeaderView(R.layout.nav_user_header)
            navHeader.malUserText.text=malUser!!.username
            if(malUser!!.image_url != null) navHeader.malUserImage.loadWithGlide(malUser!!.image_url!!,navHeader.malUserProgress)
            else navHeader.malUserImage.setImageDrawable(resources.getDrawable(R.drawable.ic_person_black_24dp,theme))

            setHeaderListeners(navHeader)
        }else{
            navHeader=navigationView.inflateHeaderView(R.layout.nav_no_user_header)
            navHeader.malUserAddButton.setOnClickListener {
                commonViewModel.getUserProfile("mrntlu",object:CoroutinesErrorHandler{
                    override fun onError(message: String) {
                        GlobalScope.launch(Dispatchers.Main){

                        }
                    }
                }).observe(this, Observer {
                    malUser=it
                    setHeader()
                })
            }
        }

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
    }

    override fun onDestroy() {
        setSupportActionBar(null)
        adView.adListener=null
        super.onDestroy()
    }
}