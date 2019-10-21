package com.mrntlu.myanimeinfo2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.llollox.androidtoggleswitch.widgets.ToggleSwitch
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewMangaListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
        animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
        mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)

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
    }

    private fun setupObservers() {
        setTopAiringAnimeObserver()
        setTopMangaObserver()
        setAiringTodayObserver()
    }

    private fun setTopAiringAnimeObserver()= animeViewModel.getTopAnimes(1,resources.getStringArray(R.array.topAnimeSubtypes)[1].toLowerCase(Locale.ENGLISH),
        object :CoroutinesErrorHandler{
            override fun onError(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    topAiringAdapter.submitError(message)
                }
            }
        }).observe(viewLifecycleOwner,Observer{
        topAiringAdapter.submitList(it.top)
    })

    private fun setTopMangaObserver()=mangaViewModel.getTopMangas(1,"",
        object :CoroutinesErrorHandler{
            override fun onError(message: String) {
                GlobalScope.launch(Dispatchers.Main) {
                    topMangaAdapter.submitError(message)
                }
            }
        }).observe(viewLifecycleOwner, Observer {
        topMangaAdapter.submitList(it.top)
    })

    private fun setAiringTodayObserver()=animeViewModel.getAnimeSchedule(object :CoroutinesErrorHandler{
        override fun onError(message: String) {
            GlobalScope.launch(Dispatchers.Main) {
                todayAiringAdapter.submitError(message)
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
            topAiringAdapter=PreviewAnimeListAdapter(interaction = object : PreviewAnimeListAdapter.Interaction {
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
            topMangaAdapter= PreviewMangaListAdapter(interaction = object : PreviewMangaListAdapter.Interaction {
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
            todayAiringAdapter=PreviewAnimeListAdapter(interaction = object : PreviewAnimeListAdapter.Interaction {
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
