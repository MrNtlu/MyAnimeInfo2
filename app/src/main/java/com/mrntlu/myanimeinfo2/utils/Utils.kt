package com.mrntlu.myanimeinfo2.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.myanimeinfo2.R
import java.util.*

object Constants{
    const val TIME_OUT=5000L
    const val THEME_PREF_NAME="appTheme"
    const val DARK_THEME=0
    const val LIGHT_THEME=1
}

fun View.setGone(){
    this.visibility=View.GONE
}

fun View.setVisible(){
    this.visibility=View.VISIBLE
}

fun String.makeCapital()=substring(0,1).toUpperCase(Locale.ENGLISH)+substring(1)

fun showToast(context: Context?, message: String) = Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

fun printLog(tag: String = "Test",message:String)= Log.d(tag,message)

fun isInternetAvailable(context: Context): Boolean {
    var result = false
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    cm?.run {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
    }
    return result
}

fun ImageView.loadWithGlide(imageUrl:String,progressBar: ProgressBar)= Glide.with(context).load(imageUrl).addListener(object :
    RequestListener<Drawable> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
        this@loadWithGlide.setImageResource(R.drawable.ic_no_picture)
        progressBar.setGone()
        return false
    }

    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        progressBar.setGone()
        return false
    }

}).into(this)

fun Activity.writeToPref(username:String){
    val sharedPref=getPreferences(Context.MODE_PRIVATE).edit()
    sharedPref.putString("username",username)
    sharedPref.apply()
}

fun Activity.readFromPref(): String? {
    val sharedPref=getPreferences(Context.MODE_PRIVATE)
    return sharedPref.getString("username",null)
}

fun Activity.deletePref(){
    getPreferences(Context.MODE_PRIVATE).edit().remove("username").apply()
}

fun View.hideKeyboard() {
    val inputMethodManager =
        (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = (context as Activity).currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}