package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.CharacterListAdapter
import com.mrntlu.myanimeinfo2.models.CharacterBodyResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class CharactersFragment(private val malID:Int,private val dataType:DataType): Fragment() {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var characterListAdapter: CharacterListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (dataType==MANGA){
            mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
        }else{
            animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
        }

        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        if (dataType==MANGA){
            mangaViewModel.getMangaCharactersByID(malID).observe(viewLifecycleOwner, Observer {
                printLog(message = "Manga Chars $it")
                characterListAdapter.submitList(it.characters)
            })
        }else{
            animeViewModel.getAnimeCharactersByID(malID).observe(viewLifecycleOwner, Observer {
                printLog(message = "Anime Chars $it")
                characterListAdapter.submitList(it.characters)
            })
        }
    }

    private fun setupRecyclerView()= fragmentRV.apply {
        layoutManager=LinearLayoutManager(this.context)
        characterListAdapter= CharacterListAdapter(object :CharacterListAdapter.Interaction{
            override fun onItemSelected(position: Int, item: CharacterBodyResponse) {
                printLog(message = "Item ${item.mal_id} ${item.name}")
            }
        })
        adapter=characterListAdapter
    }

}
