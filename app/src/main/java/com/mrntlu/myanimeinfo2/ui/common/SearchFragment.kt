package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.BaseAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewAnimeListAdapter
import com.mrntlu.myanimeinfo2.adapters.PreviewMangaListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.*
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.goUpFAB
import kotlinx.android.synthetic.main.fragment_search.searchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var searchMangaAdapter: PreviewMangaListAdapter
    private lateinit var searchAnimeAdapter: PreviewAnimeListAdapter
    private lateinit var navController: NavController
    private lateinit var dataType:DataType

    private var isLoading=false
    private var isSearching=false
    private var pageNum=1
    private var mQuery=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataType=DataType.getByCode(it.getInt("data_type"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        if (dataType== MANGA) mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
        else animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)

        setSearchView()
        setListeners()
    }

    private fun setListeners() {
        goUpFAB.setOnClickListener {
            goUpFAB.hide()
            searchRV.scrollToPosition(0)
        }
    }

    private fun setSearchView() {
        pageNum=1
        searchView.isIconified=false

        searchView.setOnCloseListener{true}

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?)=true

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (isSearching) showToast(searchView.context,"Searching...")
                    else if (!isSearching && it.length>2 && it.isNotEmpty() && it.isNotBlank()){
                        mQuery=it
                        isSearching=true
                        searchAnim.setVisible()
                        setupRecyclerView()

                        setupObservers()
                        searchView.clearFocus()
                    }
                    else showToast(searchView.context,"Query should be longer than 2")
                }
                return false
            }
        })
    }

    private fun setupObservers(){
        if (dataType==MANGA){
            mangaViewModel.getMangaBySearch(mQuery,pageNum,this).observe(viewLifecycleOwner, Observer {
                if (pageNum==1) {
                    isSearching=false
                    searchMangaAdapter.submitList(it.results)
                    searchAnim.setGone()
                }else{
                    isLoading=false
                    searchMangaAdapter.submitPaginationList(it.results)
                }
            })
        }else{
            animeViewModel.getAnimeBySearch(mQuery,pageNum,this).observe(viewLifecycleOwner, Observer {
                if (pageNum==1) {
                    isSearching=false
                    searchAnimeAdapter.submitList(it.results)
                    searchAnim.setGone()
                }else{
                    isLoading=false
                    searchAnimeAdapter.submitPaginationList(it.results)
                }
            })
        }
    }

    private fun setupRecyclerView(){
        searchRV.apply {
            val gridLayoutManager=GridLayoutManager(context,2)
            gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (dataType==MANGA) if (searchMangaAdapter.getItemViewType(position)==searchMangaAdapter.ITEM_HOLDER) 1 else 2
                    else if (searchAnimeAdapter.getItemViewType(position)==searchAnimeAdapter.ITEM_HOLDER) 1 else 2
                }
            }
            layoutManager=gridLayoutManager
            adapter=if (dataType==ANIME) setAnimeAdapter() else setMangaAdapter()

            var isScrolling=false
            this.addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    isScrolling = newState != AbsListView.OnScrollListener.SCROLL_STATE_IDLE //checks if currently scrolling
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy<=-8 && gridLayoutManager.findLastVisibleItemPosition()>15) goUpFAB.show()
                    else if (gridLayoutManager.findLastVisibleItemPosition()>15 && dy in -7..7) if (goUpFAB.isVisible) goUpFAB.show() else goUpFAB.hide()
                    else goUpFAB.hide()

                    if (isScrolling && gridLayoutManager.findLastCompletelyVisibleItemPosition()==(if (dataType==ANIME) searchAnimeAdapter.itemCount-1 else searchMangaAdapter.itemCount-1) && !isLoading){
                        isLoading=true
                        pageNum++
                        if (dataType==ANIME){
                            searchAnimeAdapter.submitPaginationLoading()
                            (this@apply).scrollToPosition(searchAnimeAdapter.itemCount-1)
                        }
                        else {
                            searchMangaAdapter.submitPaginationLoading()
                            (this@apply).scrollToPosition(searchMangaAdapter.itemCount-1)
                        }

                        setupObservers()
                    }
                }
            })
        }
    }

    private fun setAnimeAdapter():PreviewAnimeListAdapter{
        searchAnimeAdapter=PreviewAnimeListAdapter (R.layout.cell_preview_large,object :BaseAdapter.Interaction<PreviewAnimeResponse>{
            override fun onErrorRefreshPressed() {
                searchAnimeAdapter.submitLoading()
                setupObservers()
            }

            override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_search_to_animeInfo, bundle)
            }
        })
        return searchAnimeAdapter
    }

    private fun setMangaAdapter():PreviewMangaListAdapter{
        searchMangaAdapter= PreviewMangaListAdapter(R.layout.cell_preview_large,object : BaseAdapter.Interaction<PreviewMangaResponse>{
            override fun onErrorRefreshPressed() {
                searchMangaAdapter.submitLoading()
                setupObservers()
            }

            override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_search_to_mangaInfo, bundle)
            }
        })
        return searchMangaAdapter
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            searchAnim.setGone()
            if (pageNum==1){
                isSearching=false
                if (dataType==MANGA) searchMangaAdapter.submitError(message)
                else searchAnimeAdapter.submitError(message)
            }
            else{
                isLoading=false
                pageNum--
                if (dataType==MANGA) searchMangaAdapter.submitPaginationError()
                else searchAnimeAdapter.submitPaginationError()

                showToast(context,"Failed to load more. $message")
            }
        }
    }

    override fun onDestroyView() {
        searchRV.adapter=null
        view?.hideKeyboard()
        searchView.setOnCloseListener(null)
        searchView.setOnQueryTextListener(null)
        super.onDestroyView()
    }
}
