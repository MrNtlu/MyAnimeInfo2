package com.mrntlu.myanimeinfo2.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.ui.others.MainActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

object Constants{
    const val TIME_OUT=5000L
}

fun View.setGone(){
    this.visibility=View.GONE
}

fun View.setVisible(){
    this.visibility=View.VISIBLE
}

fun String.makeCapital()=substring(0,1).toUpperCase(Locale.ENGLISH)+substring(1)

fun View.setToolbarTitle(title:String){
    val activity: MainActivity?=(context as MainActivity)
    if (activity!=null) activity.toolbar.title = title
}

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

fun SearchView.getStringQuery()=this.query.toString()

fun View.hideKeyboard() {
    val inputMethodManager =
        (context as Activity).getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    val currentFocusedView = (context as Activity).currentFocus
    currentFocusedView?.let {
        inputMethodManager.hideSoftInputFromWindow(
            currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}