package com.mrntlu.myanimeinfo2.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast

fun View.setGone(){
    this.visibility=View.GONE
}

fun View.setVisible(){
    this.visibility=View.VISIBLE
}

fun showToast(context: Context?, message: String) = Toast.makeText(context,message,Toast.LENGTH_SHORT).show()

fun printLog(tag: String = "Test",message:String)= Log.d(tag,message)
