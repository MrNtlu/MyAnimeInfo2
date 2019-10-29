package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.view.size
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.BaseAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewMangaListAdapter
import com.mrntlu.myanimeinfo2.adapters.TopListTypeAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.makeCapital
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setToolbarTitle
import com.mrntlu.myanimeinfo2.utils.showToast
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_top_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class TopListFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var navController: NavController
    private lateinit var dataType:DataType
    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var topAnimeListAdapter: PreviewAnimeListAdapter
    private lateinit var topMangaListAdapter: PreviewMangaListAdapter

    private var subType=""
    private var isLoading=false
    private var pageNum=1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataType=DataType.getByCode(it.getInt("data_type"))
            subType=it.getString("sub_type","")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_top_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        setupRecyclerView()
        setListeners()

        if (dataType==ANIME){
            animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
            setAnimeObserver()
        }else{
            mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
            setMangaObserver()
        }
    }

    private fun setListeners() {
        goUpFAB.setOnClickListener {
            goUpFAB.hide()
            topListRV.scrollToPosition(0)
        }
    }

    private fun setupRecyclerView(){
        pageNum=1

        topListTypeRV.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter=TopListTypeAdapter(resources.getStringArray(if (dataType==ANIME) R.array.topAnimeSubtypes else R.array.topMangaSubtypes).toCollection(ArrayList()),object :TopListTypeAdapter.Interaction{
                override fun onItemSelected(position: Int, item: String) {
                    if (if (position==0) subType!="" else subType!=item) {
                        subType = if (position == 0) "" else item.toLowerCase(Locale.ENGLISH)
                        pageNum=1
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
                    return if (dataType==ANIME){
                        if (topAnimeListAdapter.getItemViewType(position)==topAnimeListAdapter.ITEM_HOLDER) 1 else 2
                    } else{
                        if (topMangaListAdapter.getItemViewType(position)==topMangaListAdapter.ITEM_HOLDER) 1 else 2
                    }
                }
            }
            layoutManager=gridLayoutManager
            adapter=if (dataType==ANIME) setAnimeAdapter() else setMangaAdapter()

            var isScrolling=false
            this.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    isScrolling = newState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE //checks if currently scrolling
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy<=-8 && gridLayoutManager.findLastVisibleItemPosition()>15) goUpFAB.show()
                    else if (gridLayoutManager.findLastVisibleItemPosition()>15 && dy in -7..7) if (goUpFAB.isVisible) goUpFAB.show() else goUpFAB.hide()
                    else goUpFAB.hide()


                    if (isScrolling && gridLayoutManager.findLastCompletelyVisibleItemPosition()==(if (dataType==ANIME) topAnimeListAdapter.itemCount-1 else topMangaListAdapter.itemCount-1) && !isLoading){
                        isLoading=true
                        pageNum++
                        if (dataType==ANIME){
                            topAnimeListAdapter.submitPaginationLoading()
                            (this@apply).scrollToPosition(topAnimeListAdapter.itemCount-1)
                            setAnimeObserver()
                        }
                        else {
                            topMangaListAdapter.submitPaginationLoading()
                            (this@apply).scrollToPosition(topMangaListAdapter.itemCount-1)
                            setMangaObserver()
                        }
                    }
                }
            })
        }
    }

    private fun setMangaAdapter(): PreviewMangaListAdapter {
        topMangaListAdapter= PreviewMangaListAdapter(R.layout.cell_preview_large,object : BaseAdapter.Interaction<PreviewMangaResponse>{
            override fun onErrorRefreshPressed() {
                topMangaListAdapter.submitLoading()
                setMangaObserver()
            }

            override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_topList_to_mangaInfo, bundle)
            }
        })
        return topMangaListAdapter
    }

    private fun setAnimeAdapter(): PreviewAnimeListAdapter {
        topAnimeListAdapter=PreviewAnimeListAdapter(R.layout.cell_preview_large,object :BaseAdapter.Interaction<PreviewAnimeResponse>{
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
        animeViewModel.getTopAnimes(pageNum,subType,this).observe(viewLifecycleOwner, Observer {
            if (pageNum==1) topAnimeListAdapter.submitList(it.top)
            else{
                isLoading=false
                topAnimeListAdapter.submitPaginationList(it.top)
            }
        })
    }

    private fun setMangaObserver(){
        mangaViewModel.getTopMangas(pageNum,subType,this).observe(viewLifecycleOwner, Observer {
            if (pageNum==1) topMangaListAdapter.submitList(it.top)
            else {
                isLoading=false
                topMangaListAdapter.submitPaginationList(it.top)
            }
        })
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            printLog(message = message)
            if (pageNum == 1){
                if (dataType == ANIME) topAnimeListAdapter.submitError(message)
                else topMangaListAdapter.submitError(message)
            }
            else {
                isLoading = false
                pageNum--
                if (dataType==ANIME) topAnimeListAdapter.submitPaginationError()
                else topMangaListAdapter.submitPaginationError()
                showToast(context,"Failed to load more. $message")
            }
        }
    }

    override fun onDestroyView() {
        topListRV.adapter=null
        topListTypeRV.adapter=null
        super.onDestroyView()
    }
}
