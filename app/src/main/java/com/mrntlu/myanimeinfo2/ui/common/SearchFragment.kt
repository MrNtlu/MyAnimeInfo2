package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.mrntlu.myanimeinfo2.R
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
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.searchView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var searchMangaAdapter: PreviewMangaListAdapter
    private lateinit var searchAnimeAdapter: PreviewAnimeListAdapter
    private lateinit var navController: NavController
    private lateinit var dataType:DataType

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
    }

    private fun setSearchView() {
        searchView.isIconified=false

        searchView.setOnCloseListener{true}

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?)=true

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    if (it.length>2 && it.isNotEmpty() && it.isNotBlank()){
                        searchAnim.setVisible()
                        if (dataType==MANGA) setupMangaRecyclerView()
                        else setupAnimeRecyclerView()

                        setupObservers(it)
                        searchView.clearFocus()
                    }
                    else showToast(searchView.context,"Query should be longer than 2")
                }
                return false
            }
        })
    }

    private fun setupObservers(query:String){
        if (dataType==MANGA){
            mangaViewModel.getMangaBySearch(query,1,object :CoroutinesErrorHandler{
                override fun onError(message: String){
                    GlobalScope.launch(Dispatchers.Main) {
                        searchMangaAdapter.submitError(message)
                        searchAnim.setGone()
                    }
                }
            }).observe(viewLifecycleOwner, Observer {
                searchMangaAdapter.submitList(it.results)
                searchAnim.setGone()
            })
        }else{
            animeViewModel.getAnimeBySearch(query,1,object :CoroutinesErrorHandler{
                override fun onError(message: String) {
                    GlobalScope.launch(Dispatchers.Main) {
                        searchAnimeAdapter.submitError(message)
                        searchAnim.setGone()
                    }
                }
            }).observe(viewLifecycleOwner, Observer {
                searchAnimeAdapter.submitList(it.results)
                searchAnim.setGone()
            })
        }
    }

    private fun setupMangaRecyclerView(){
        searchRV.apply {
            searchMangaAdapter= PreviewMangaListAdapter(R.layout.cell_preview_large,object :PreviewMangaListAdapter.Interaction{
                override fun onErrorRefreshPressed() {
                    searchMangaAdapter.submitLoading()
                    setupObservers(searchView.getStringQuery())
                }

                override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                    val bundle = bundleOf("mal_id" to item.mal_id)
                    navController.navigate(R.id.action_search_to_mangaInfo, bundle)
                }
            })
            val gridLayoutManager=GridLayoutManager(context,2)
            gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (searchMangaAdapter.getItemViewType(position)==searchMangaAdapter.PREVIEW_HOLDER) 1 else 2
                }
            }
            layoutManager=gridLayoutManager
            adapter=searchMangaAdapter
        }
    }

    private fun setupAnimeRecyclerView(){
        searchRV.apply {
            searchAnimeAdapter=PreviewAnimeListAdapter (R.layout.cell_preview_large,object :PreviewAnimeListAdapter.Interaction{
                override fun onErrorRefreshPressed() {
                    searchAnimeAdapter.submitLoading()
                    setupObservers(searchView.getStringQuery())
                }

                override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                    val bundle = bundleOf("mal_id" to item.mal_id)
                    navController.navigate(R.id.action_search_to_animeInfo, bundle)
                }
            })

            val gridLayoutManager=GridLayoutManager(context,2)
            gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (searchAnimeAdapter.getItemViewType(position)==searchAnimeAdapter.PREVIEW_HOLDER) 1 else 2
                }
            }
            layoutManager=gridLayoutManager
            adapter=searchAnimeAdapter
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
