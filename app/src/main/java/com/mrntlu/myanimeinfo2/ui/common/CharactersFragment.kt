package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.CharacterListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.CharacterBodyResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CharactersFragment(private val malID:Int,private val dataType:DataType): Fragment() {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var navController: NavController
    private lateinit var characterListAdapter: CharacterListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        if (dataType==MANGA) mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
        else animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)


        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        if (dataType==MANGA){
            mangaViewModel.getMangaCharactersByID(malID,object :CoroutinesErrorHandler{
                override fun onError(message: String) {
                    GlobalScope.launch(Dispatchers.Main) {
                        submitError(message)
                    }
                }

            }).observe(viewLifecycleOwner, Observer {
                characterListAdapter.submitList(it.characters)
            })
        }else{
            animeViewModel.getAnimeCharactersByID(malID,object :CoroutinesErrorHandler{
                override fun onError(message: String) {
                    GlobalScope.launch(Dispatchers.Main) {
                        submitError(message)
                    }
                }
            }).observe(viewLifecycleOwner, Observer {
                characterListAdapter.submitList(it.characters)
            })
        }
    }

    private fun submitError(message:String)=GlobalScope.launch(Dispatchers.Main) {
        characterListAdapter.submitError(message)
    }

    private fun setupRecyclerView()= fragmentRV.apply {
        layoutManager=LinearLayoutManager(context)
        characterListAdapter= CharacterListAdapter(object :CharacterListAdapter.Interaction{
            override fun onErrorRefreshPressed() {
                characterListAdapter.submitLoading()
                setupObservers()
            }

            override fun onItemSelected(position: Int, item: CharacterBodyResponse) {
                val bundle= bundleOf("mal_id" to item.mal_id)
                navController.navigate(if (dataType==MANGA) R.id.action_mangaInfo_to_characterInfo else R.id.action_animeInfo_to_characterInfo,bundle)
            }
        })
        adapter=characterListAdapter
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
