package com.mrntlu.myanimeinfo2.ui.manga

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlin.properties.Delegates

class MangaInfoFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var commonViewModel: CommonViewModel
    private var malID by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            malID=it.getInt("mal_id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manga_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(AnimeViewModel::class.java)
        mangaViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(MangaViewModel::class.java)
        commonViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(CommonViewModel::class.java)

        setupObservers()
    }

    private fun setupObservers() {
        mangaViewModel.getMangaByID(malID).observe(viewLifecycleOwner, Observer {
            printLog(message = it.toString())
        })
    }
}
