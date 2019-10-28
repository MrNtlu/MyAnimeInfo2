package com.mrntlu.myanimeinfo2.ui.anime

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
import androidx.recyclerview.widget.GridLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.BaseAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.utils.makeCapital
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.showToast
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import kotlinx.android.synthetic.main.fragment_anime_season.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class AnimeSeasonFragment : Fragment(), CoroutinesErrorHandler, BaseAdapter.Interaction<PreviewAnimeResponse> {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var seasonAnimeAdapter: PreviewAnimeListAdapter
    private lateinit var navController: NavController

    private var mSeason="winter"
    private var mYear="later"
    private var isLoading=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_anime_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        animeViewModel=ViewModelProviders.of(this).get(AnimeViewModel::class.java)

        setupRecyclerView()
        setupObserver(mYear,"")
        setSpinners()
    }

    private fun setSpinners() {
        seasonSpinner.setItems(resources.getStringArray(R.array.seasonNames).asList().map {it.makeCapital()})

        val currentYear=getCurrentYear()
        val years=(currentYear downTo currentYear-30).map { it.toString() }.toMutableList().also { it.add(0,"Upcoming") }
        yearSpinner.setItems(years)

        seasonSpinner.setOnItemSelectedListener { view, position, id, item ->
            if (!isLoading && mYear != "later"){
                seasonAnimeAdapter.submitLoading()
                isLoading=true
                mSeason=item.toString().toLowerCase(Locale.ENGLISH)
                setupObserver(mYear,mSeason)
            }
        }

        yearSpinner.setOnItemSelectedListener { view, position, id, item ->
            if (!isLoading) {
                isLoading=true
                seasonAnimeAdapter.submitLoading()
                if (position == 0) {
                    mYear = "later"
                    setupObserver("later", "")
                } else {
                    mYear = item.toString()
                    setupObserver(mYear, mSeason)
                }
            }
        }
    }

    private fun getCurrentYear()= Calendar.getInstance().get(Calendar.YEAR)

    private fun setupObserver(year:String,season:String){
        animeViewModel.getAnimeBySeason(year,season,this).observe(viewLifecycleOwner, Observer {
            seasonAnimeAdapter.submitList(it.anime)
            isLoading=false
        })
    }

    private fun setupRecyclerView()=animeSeasonRV.apply {
        val gridLayoutManager= GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                 return if(seasonAnimeAdapter.getItemViewType(position)==seasonAnimeAdapter.ITEM_HOLDER) 1 else 2
            }
        }
        layoutManager=gridLayoutManager
        seasonAnimeAdapter= PreviewAnimeListAdapter(R.layout.cell_preview_large,this@AnimeSeasonFragment)
        adapter=seasonAnimeAdapter
    }

    override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
        val bundle = bundleOf("mal_id" to item.mal_id)
        navController.navigate(R.id.action_animeSeason_to_animeInfo, bundle)
    }

    override fun onErrorRefreshPressed() {
        seasonAnimeAdapter.submitLoading()
        if (mYear=="later") setupObserver(mYear,"")
        else setupObserver(mYear,mSeason)
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            seasonAnimeAdapter.submitError(message)
            isLoading = false
        }
    }

    override fun onDestroyView() {
        animeSeasonRV.adapter=null
        super.onDestroyView()
    }
}
