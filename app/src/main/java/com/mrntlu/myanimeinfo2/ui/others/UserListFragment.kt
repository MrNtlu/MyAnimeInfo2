package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewMangaListAdapter
import com.mrntlu.myanimeinfo2.adapters.UserAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.UserMangaListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.utils.makeCapital
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.fragment_anime_season.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/*
1 Watching, reading
2 Completed
3 OnHold
4 Dropped
6 Plan to watch, plan to read
 */
class UserListFragment : Fragment(), CoroutinesErrorHandler{

    private lateinit var navController: NavController
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var username:String
    private lateinit var dataType: DataType
    private lateinit var animeList:List<UserAnimeListBody>
    private lateinit var mangaList:List<UserMangaListBody>
    private lateinit var animeAdapter: UserAnimeListAdapter
    private lateinit var mangaAdapter: UserMangaListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username=it.getString("username","")
            dataType= DataType.getByCode(it.getInt("data_type"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_anime_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        commonViewModel= ViewModelProviders.of(this).get(CommonViewModel::class.java)
        seasonSpinner.setGone()
        yearSpinner.isEnabled=false

        setRecyclerView()
        setSpinner()
        setupObservers()
    }

    private fun setupObservers() {
        if (dataType== ANIME){
            commonViewModel.getUserAnimeList(username,this).observe(viewLifecycleOwner, Observer {
                animeList=it.anime
                animeAdapter.submitList(animeList)
                yearSpinner.isEnabled=true
            })
        }else{
            commonViewModel.getUserMangaList(username,this).observe(viewLifecycleOwner, Observer {
                mangaList=it.manga
                mangaAdapter.submitList(mangaList)
                yearSpinner.isEnabled=true
            })
        }
    }

    private fun setSpinner() {
        if (dataType==ANIME){
            yearSpinner.setItems(resources.getStringArray(R.array.animeListFilter).asList().map { it.makeCapital() })
        }else{
            yearSpinner.setItems(resources.getStringArray(R.array.mangaListFilter).asList().map { it.makeCapital() })
        }
        yearSpinner.setOnItemSelectedListener { _, position, _, _ ->
            filterData(position)
        }
    }

    private fun setRecyclerView()=animeSeasonRV.apply {
        layoutManager= LinearLayoutManager(context)
        if (dataType==ANIME){
            animeAdapter= UserAnimeListAdapter(object :Interaction<UserAnimeListBody>{
                override fun onItemSelected(position: Int, item: UserAnimeListBody) {

                }

                override fun onErrorRefreshPressed() {
                    animeAdapter.submitLoading()
                    setupObservers()
                }

            })
            adapter=animeAdapter
        }else{
            mangaAdapter= UserMangaListAdapter(object :Interaction<UserMangaListBody>{
                override fun onItemSelected(position: Int, item: UserMangaListBody) {

                }

                override fun onErrorRefreshPressed() {
                    mangaAdapter.submitLoading()
                    setupObservers()
                }

            })
            adapter=mangaAdapter
        }
    }

    private fun filterData(position:Int){
        if (dataType==ANIME){
            if (position==0) animeAdapter.submitList(animeList)
            else{
                val filteredList = animeList.filter {
                    when (position) {
                        1 -> it.watching_status == 1
                        2 -> it.watching_status == 2
                        3 -> it.watching_status == 3
                        4 -> it.watching_status == 4
                        else -> it.watching_status == 6
                    }
                }
                animeAdapter.submitList(filteredList)
            }
        }else{
            if (position==0) mangaAdapter.submitList(mangaList)
            else{
                val filteredList = mangaList.filter {
                        when (position) {
                            1 -> it.reading_status == 1
                            2 -> it.reading_status == 2
                            3 -> it.reading_status == 3
                            4 -> it.reading_status == 4
                            else -> it.reading_status == 6
                        }
                    }
                mangaAdapter.submitList(filteredList)
            }
        }
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main){
            yearSpinner.isEnabled=false
            if (dataType==ANIME) animeAdapter.submitError(message)
            else mangaAdapter.submitError(message)
        }
    }

    override fun onDestroyView() {
        animeSeasonRV.adapter=null
        super.onDestroyView()
    }
}
