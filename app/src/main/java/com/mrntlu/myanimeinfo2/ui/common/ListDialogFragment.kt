package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
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
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DataType.*
import com.mrntlu.myanimeinfo2.models.DialogType
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.showToast
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_genre_dialog.*
import kotlinx.android.synthetic.main.fragment_genre_dialog.goUpFAB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class ListDialogFragment: DialogFragment(),CoroutinesErrorHandler {

    private lateinit var animeViewModel: AnimeViewModel
    private lateinit var mangaViewModel: MangaViewModel
    private lateinit var navController: NavController
    private lateinit var animeGenreAdapter:PreviewAnimeListAdapter
    private lateinit var mangaGenreAdapter:PreviewMangaListAdapter
    private lateinit var dataType:DataType
    private lateinit var dialogType:DialogType
    private lateinit var genreName:String

    private var malID by Delegates.notNull<Int>()
    private var isLoading=false
    private var pageNum=1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = R.style.MyDialogAnimation
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            genreName=it.getString("genre_name","")
            dataType=DataType.getByCode(it.getInt("data_type"))
            dialogType=DialogType.getByCode(it.getInt("dialog_type"))
            malID=it.getInt("mal_id")
        }
        setStyle(STYLE_NORMAL,R.style.FullScreenLightDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_genre_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window!!.setLayout(width, height)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragment?.let {
            if (it.view!=null) navController=Navigation.findNavController(it.view!!)
        }

        setupUI()
        setupRecyclerView()
        if (dataType== MANGA){
            mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)
            setGenreMangaObserver()
        } else{
            animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)
            if (dialogType==DialogType.GENRE) setGenreAnimeObserver()
            else setProducerAnimeObserver()
        }
    }

    private fun setupUI(){
        dialogCloseButton.setOnClickListener {
            this.dismiss()
        }
        dialogGenreNameText.text=genreName

        goUpFAB.setOnClickListener {
            goUpFAB.hide()
            genreRV.scrollToPosition(0)
        }
    }

    private fun setGenreMangaObserver() {
        mangaViewModel.getMangaByGenre(malID,pageNum,this).observe(viewLifecycleOwner, Observer {
            if (pageNum==1) mangaGenreAdapter.submitList(it.manga)
            else{
                isLoading=false
                mangaGenreAdapter.submitPaginationList(it.manga)
            }
        })
    }

    private fun setGenreAnimeObserver() {
        animeViewModel.getAnimeByGenre(malID,pageNum,this).observe(viewLifecycleOwner, Observer {
            if (pageNum==1) animeGenreAdapter.submitList(it.anime)
            else{
                isLoading=false
                animeGenreAdapter.submitPaginationList(it.anime)
            }
        })
    }

    private fun setProducerAnimeObserver(){
        animeViewModel.getProducerInfoByID(malID,this).observe(viewLifecycleOwner, Observer {
            if (pageNum==1) animeGenreAdapter.submitList(it.anime)
            else{
                isLoading=false
                animeGenreAdapter.submitPaginationList(it.anime)
            }
        })
    }

    private fun setupRecyclerView()=genreRV.apply {
        val gridLayoutManager=GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (dataType==MANGA){
                    if (mangaGenreAdapter.getItemViewType(position)==mangaGenreAdapter.ITEM_HOLDER) 1 else 2
                } else{
                    if (animeGenreAdapter.getItemViewType(position)==animeGenreAdapter.ITEM_HOLDER) 1 else 2
                }
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

                if (isScrolling && gridLayoutManager.findLastCompletelyVisibleItemPosition()==(if (dataType==ANIME) animeGenreAdapter.itemCount-1 else mangaGenreAdapter.itemCount-1) && !isLoading){
                    isLoading=true
                    pageNum++
                    if (dataType==ANIME){
                        animeGenreAdapter.submitPaginationLoading()
                        (this@apply).scrollToPosition(animeGenreAdapter.itemCount-1)

                        if (dialogType==DialogType.GENRE) setGenreAnimeObserver()
                        else setProducerAnimeObserver()
                    }
                    else {
                        mangaGenreAdapter.submitPaginationLoading()
                        (this@apply).scrollToPosition(mangaGenreAdapter.itemCount-1)

                        setGenreMangaObserver()
                    }
                }
            }
        })
    }

    private fun setMangaAdapter(): PreviewMangaListAdapter {
        mangaGenreAdapter=PreviewMangaListAdapter(R.layout.cell_preview_large,object : Interaction<PreviewMangaResponse> {
            override fun onErrorRefreshPressed() {
                mangaGenreAdapter.submitLoading()
                setGenreMangaObserver()
            }

            override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_genreDialog_to_mangaInfo, bundle)
                this@ListDialogFragment.dismiss()
            }
        })
        return mangaGenreAdapter
    }

    private fun setAnimeAdapter(): PreviewAnimeListAdapter {
        animeGenreAdapter=PreviewAnimeListAdapter (R.layout.cell_preview_large,object : Interaction<PreviewAnimeResponse>{
            override fun onErrorRefreshPressed() {
                animeGenreAdapter.submitLoading()
                if (dialogType==DialogType.GENRE) setGenreAnimeObserver()
                else setProducerAnimeObserver()
            }

            override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_genreDialog_to_animeInfo, bundle)
                this@ListDialogFragment.dismiss()
            }
        })
        return animeGenreAdapter
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main){
            if (pageNum==1) {
                if (dataType == MANGA) mangaGenreAdapter.submitError(message)
                else animeGenreAdapter.submitError(message)
            }else{
                isLoading=false
                pageNum--
                if (dataType==MANGA) mangaGenreAdapter.submitPaginationError()
                else animeGenreAdapter.submitPaginationError()

                showToast(context,"Failed to load more. $message")
            }
        }
    }

    override fun onDestroyView() {
        genreRV.adapter=null
        super.onDestroyView()
    }
}
