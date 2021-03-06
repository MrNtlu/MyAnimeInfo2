package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewMangaListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainFragment : Fragment(){

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var topAiringAdapter:PreviewAnimeListAdapter
    private lateinit var topMangaAdapter:PreviewMangaListAdapter
    private lateinit var todayAiringAdapter:PreviewAnimeListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel = ViewModelProvider(this).get(AnimeViewModel::class.java)
        mangaViewModel = ViewModelProvider(this).get(MangaViewModel::class.java)

        setToggle()
        setListeners()
        setupRecyclerView()
        setupObservers()
    }

    private fun setToggle() {
        dataTypeToggle.setCheckedPosition(0)
    }

    private fun setListeners() {
        searchView.setOnClickListener {
            searchView.isIconified=false
        }

        searchView.setOnQueryTextFocusChangeListener { _, b ->
            if (b) {
                val dataType=DataType.getByCode(dataTypeToggle.getCheckedPosition())
                val bundle= bundleOf("data_type" to dataType.code)
                navController.navigate(R.id.action_main_to_search,bundle)
            }
            searchView.isIconified=true
        }

        topAiringText.setOnClickListener {
            val bundle= bundleOf("data_type" to DataType.ANIME.code,"sub_type" to "airing")
            navController.navigate(R.id.action_main_to_topList,bundle)
        }

        topMangaText.setOnClickListener {
            val bundle= bundleOf("data_type" to DataType.MANGA.code)
            navController.navigate(R.id.action_main_to_topList,bundle)
        }

        airingTodayText.setOnClickListener {
            navController.navigate(R.id.action_main_to_schedule)
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch{
            whenResumed {
                setTopAiringAnimeObserver()
                delay(600)
                setTopMangaObserver()
                delay(600)
                setAiringTodayObserver()
            }
        }
    }

    private fun setTopAiringAnimeObserver()=animeViewModel.getTopAnimes(1,resources.getStringArray(R.array.topAnimeSubtypes)[1].toLowerCase(Locale.ENGLISH),
        object :CoroutinesErrorHandler{
            override fun onError(message: String) {
                viewLifecycleOwner.lifecycleScope.launch {
                    whenResumed {
                        topAiringAdapter.submitError(message)
                    }
                }
            }
        }).observe(viewLifecycleOwner,Observer{
        topAiringAdapter.submitList(it.top)
    })

    private fun setTopMangaObserver()=mangaViewModel.getTopMangas(1,"",
        object :CoroutinesErrorHandler{
            override fun onError(message: String) {
                viewLifecycleOwner.lifecycleScope.launch {
                    whenResumed {
                        topMangaAdapter.submitError(message)
                    }
                }
            }
        }).observe(viewLifecycleOwner, Observer {
        topMangaAdapter.submitList(it.top)
    })

    private fun setAiringTodayObserver()=animeViewModel.getAnimeSchedule(object :CoroutinesErrorHandler{
        override fun onError(message: String) {
            viewLifecycleOwner.lifecycleScope.launch {
                whenResumed {
                    todayAiringAdapter.submitError(message)
                }
            }
        }
    }).observe(viewLifecycleOwner, Observer {
        val scheduleList=when(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){
            Calendar.MONDAY->it.monday
            Calendar.TUESDAY->it.tuesday
            Calendar.WEDNESDAY->it.wednesday
            Calendar.THURSDAY->it.thursday
            Calendar.FRIDAY->it.friday
            Calendar.SATURDAY->it.saturday
            Calendar.SUNDAY->it.sunday
            else -> it.monday
        }
        todayAiringAdapter.submitList(scheduleList)

    })

    private fun setupRecyclerView() {
        topAiringRV.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            topAiringAdapter=PreviewAnimeListAdapter(interaction = object : Interaction<PreviewAnimeResponse> {
                override fun onErrorRefreshPressed() {
                    topAiringAdapter.submitLoading()
                    setTopAiringAnimeObserver()
                }

                override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                    navigateWithBundle(item.mal_id,R.id.action_main_to_animeInfo)
                }
            })
            adapter=topAiringAdapter
        }
        topMangaRV.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            topMangaAdapter= PreviewMangaListAdapter(interaction = object : Interaction<PreviewMangaResponse> {
                override fun onErrorRefreshPressed() {
                    topMangaAdapter.submitLoading()
                    setTopMangaObserver()
                }

                override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                    navigateWithBundle(item.mal_id,R.id.action_main_to_mangaInfo)
                }
            })
            adapter=topMangaAdapter
        }
        airingTodayRV.apply {
            layoutManager=LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
            todayAiringAdapter=PreviewAnimeListAdapter(interaction = object : Interaction<PreviewAnimeResponse> {
                override fun onErrorRefreshPressed() {
                    todayAiringAdapter.submitLoading()
                    setAiringTodayObserver()
                }

                override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                    navigateWithBundle(item.mal_id,R.id.action_main_to_animeInfo)
                }
            })
            adapter=todayAiringAdapter
        }
    }

    private fun navigateWithBundle(malID:Int,navID:Int){
        val bundle= bundleOf("mal_id" to malID)
        navController.navigate(navID,bundle)
    }

    override fun onDestroyView() {
        airingTodayRV.adapter=null
        topMangaRV.adapter=null
        topAiringRV.adapter=null
        super.onDestroyView()
    }
}
