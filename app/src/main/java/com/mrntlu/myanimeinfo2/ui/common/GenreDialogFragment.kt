package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlin.properties.Delegates

class GenreDialogFragment: DialogFragment() {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var navController: NavController
    private var dataType by Delegates.notNull<Int>()
    private var malID by Delegates.notNull<Int>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataType=it.getInt("data_type")
            malID=it.getInt("mal_id")
        }
        setStyle(STYLE_NO_TITLE,R.style.FullScreenLightDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_genre_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window!!.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navController= Navigation.findNavController(view)

        if (dataType==0) setDataTypeManga(view)
        else setDataTypeAnime(view)
    }

    private fun setDataTypeManga(view: View) {
        mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)

        mangaViewModel.getMangaByGenre(malID,1).observe(viewLifecycleOwner, Observer {
            printLog(message = it.toString())
        })
    }

    private fun setDataTypeAnime(view: View) {
        animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
    }
}
