package com.mrntlu.myanimeinfo2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel=ViewModelProviders.of(this).get(AnimeViewModel::class.java)
    }
}
