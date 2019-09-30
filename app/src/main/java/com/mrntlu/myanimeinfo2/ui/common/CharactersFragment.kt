package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel

class CharactersFragment(val malID:Int,val dataType:Int): Fragment() {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dataType==0){//If 0 manga
            mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
        }else{
            animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
        }

        setupObservers()
    }

    private fun setupObservers() {
        if (dataType==0){
            mangaViewModel.getMangaCharactersByID(malID).observe(viewLifecycleOwner, Observer {
                printLog(message = "Manga Chars $it")
            })
        }else{
            animeViewModel.getAnimeCharactersByID(malID).observe(viewLifecycleOwner, Observer {
                printLog(message = "Anime Chars $it")
            })
        }
    }
}
