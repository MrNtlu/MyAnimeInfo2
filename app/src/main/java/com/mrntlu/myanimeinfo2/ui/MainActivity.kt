package com.mrntlu.myanimeinfo2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.ui.manga.MangaInfoFragment
import com.mrntlu.myanimeinfo2.utils.makeCapital
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment=nav_host_fragment as NavHostFragment
        navController=navHostFragment.navController

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            toolbar.title=when(destination.id){
                R.id.mangaInfoFragment->"Manga Info"
                R.id.animeInfoFragment->"Anime Info"
                R.id.topListFragment->{
                    if (arguments!=null){
                        val dataType=DataType.getByCode(arguments.getInt("data_Type"))
                        "Top ${dataType.name.toLowerCase(Locale.ENGLISH).makeCapital()} List"
                    } else "Top List"
                }
                R.id.scheduleAnimeFragment->"Schedule"
                R.id.genreDialogFragment->toolbar.title
                R.id.characterInfoFragment-> "Character Info"
                R.id.searchFragment->{
                    if (arguments!=null){
                        val dataType=DataType.getByCode(arguments.getInt("data_Type"))
                        "Search ${dataType.name.toLowerCase(Locale.ENGLISH).makeCapital()}"
                    } else "Search"
                }
                R.id.picturesFragment->"Pictures"
                else->""
            }
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
                R.id.main_page_menu->navController.navigate(R.id.mainFragment)
                R.id.anime_schedule->navController.navigate(R.id.scheduleAnimeFragment)
                R.id.top_anime_list->navController.navigate(R.id.topListFragment,bundleOf("data_Type" to DataType.ANIME.code))
                R.id.top_manga_list->navController.navigate(R.id.topListFragment, bundleOf("data_Type" to DataType.MANGA.code))
                R.id.anime_search->navController.navigate(R.id.searchFragment, bundleOf("data_Type" to DataType.ANIME.code))
                R.id.manga_search->navController.navigate(R.id.searchFragment, bundleOf("data_Type" to DataType.MANGA.code))
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        val toggle=ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.setToolbarNavigationClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                drawerLayout.closeDrawer(GravityCompat.START)
            }else{
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onDestroy() {
        setSupportActionBar(null)
        super.onDestroy()
    }
}