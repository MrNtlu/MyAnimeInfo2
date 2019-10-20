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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewMangaListAdapter
import com.mrntlu.myanimeinfo2.adapters.TopListTypeAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.makeCapital
import com.mrntlu.myanimeinfo2.utils.setToolbarTitle
import com.mrntlu.myanimeinfo2.utils.showToast
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_top_list.*
import java.util.*
import kotlin.collections.ArrayList

class TopListFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var navController: NavController
    private lateinit var dataType:DataType
    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var topAnimeListAdapter: PreviewAnimeListAdapter
    private lateinit var topMangaListAdapter: PreviewMangaListAdapter
    private var subType:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataType=DataType.getByCode(it.getInt("data_Type"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        //view.setToolbarTitle("Top ${dataType.name.toLowerCase(Locale.ENGLISH).makeCapital()} List")
        setupRecyclerView()

        if (dataType==ANIME){
            animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
            setAnimeObserver()
        }else{
            mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
            setMangaObserver()
        }
    }

    private fun setupRecyclerView(){
        topListTypeRV.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter=TopListTypeAdapter(resources.getStringArray(if (dataType==ANIME) R.array.topAnimeSubtypes else R.array.topMangaSubtypes).toCollection(ArrayList()),object :TopListTypeAdapter.Interaction{
                override fun onItemSelected(position: Int, item: String) {
                    if (if (position==0) subType!="" else subType!=item) {
                        subType = if (position == 0) "" else item.toLowerCase(Locale.ENGLISH)

                        if (dataType == ANIME) {
                            topAnimeListAdapter.submitLoading()
                            setAnimeObserver()
                        } else {
                            topMangaListAdapter.submitLoading()
                            setMangaObserver()
                        }
                    }else showToast(context,"${item.makeCapital()} subtype is already listed.")
                }
            })
        }

        topListRV.apply {
            val gridLayoutManager= GridLayoutManager(context,2)
            gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (dataType==ANIME) if (topAnimeListAdapter.getItemViewType(position)==topAnimeListAdapter.PREVIEW_HOLDER) 1 else 2
                    else if (topMangaListAdapter.getItemViewType(position)==topMangaListAdapter.PREVIEW_HOLDER) 1 else 2
                }
            }
            layoutManager=gridLayoutManager
            adapter=if (dataType==ANIME) setAnimeAdapter() else setMangaAdapter()
        }
    }

    private fun setMangaAdapter(): PreviewMangaListAdapter {
        topMangaListAdapter= PreviewMangaListAdapter(R.layout.cell_preview_large,object :PreviewMangaListAdapter.Interaction{
            override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_topList_to_mangaInfo, bundle)
            }

            override fun onErrorRefreshPressed() {
                topMangaListAdapter.submitLoading()
                setMangaObserver()
            }
        })
        return topMangaListAdapter
    }

    private fun setAnimeAdapter(): PreviewAnimeListAdapter {
        topAnimeListAdapter=PreviewAnimeListAdapter(R.layout.cell_preview_large,object :PreviewAnimeListAdapter.Interaction{
            override fun onErrorRefreshPressed() {
                topAnimeListAdapter.submitLoading()
                setAnimeObserver()
            }

            override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_topList_to_animeInfo, bundle)
            }
        })
        return topAnimeListAdapter
    }

    private fun setAnimeObserver(){
        animeViewModel.getTopAnimes(1,subType,this).observe(viewLifecycleOwner, Observer {
            topAnimeListAdapter.submitList(it.top)
        })
    }

    private fun setMangaObserver(){
        mangaViewModel.getTopMangas(1,subType,this).observe(viewLifecycleOwner, Observer {
            topMangaListAdapter.submitList(it.top)
        })
    }

    override fun onError(message: String) {
        if (dataType==ANIME) topAnimeListAdapter.submitError(message) else topMangaListAdapter.submitError(message)
    }

    override fun onDestroyView() {
        topListRV.adapter=null
        topListTypeRV.adapter=null
        super.onDestroyView()
    }
}
