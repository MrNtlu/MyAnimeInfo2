package com.mrntlu.myanimeinfo2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mrntlu.myanimeinfo2.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment=nav_host_fragment as NavHostFragment
        navController=navHostFragment.navController

        setDrawer()
        setStatusBarColor(R.color.white,true)
        setNavBarColor(R.color.black)
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

    fun getToolbar()=toolbar
}
