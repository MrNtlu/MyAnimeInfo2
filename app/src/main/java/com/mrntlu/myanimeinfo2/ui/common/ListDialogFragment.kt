package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
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
import com.mrntlu.myanimeinfo2.models.DialogType
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_genre_dialog.*
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

        if (dataType== MANGA){
            mangaViewModel = ViewModelProviders.of(this).get(MangaViewModel::class.java)

            setupMangaRecyclerView()
            setGenreManga()
        }
        else{
            animeViewModel = ViewModelProviders.of(this).get(AnimeViewModel::class.java)

            setupAnimeRecyclerView()
            if (dialogType==DialogType.GENRE) setGenreAnime()
            else setProducerAnime()
        }
    }

    private fun setupUI(){
        dialogCloseButton.setOnClickListener {
            this.dismiss()
        }
        dialogGenreNameText.text=genreName
    }

    private fun setGenreManga() {
        mangaViewModel.getMangaByGenre(malID,1,this).observe(viewLifecycleOwner, Observer {
            mangaGenreAdapter.submitList(it.manga)
        })
    }

    private fun setGenreAnime() {
        animeViewModel.getAnimeByGenre(malID,1,this).observe(viewLifecycleOwner, Observer {
            animeGenreAdapter.submitList(it.anime)
        })
    }

    private fun setProducerAnime(){
        animeViewModel.getProducerInfoByID(malID,this).observe(viewLifecycleOwner, Observer {
            animeGenreAdapter.submitList(it.anime)
        })
    }

    private fun setupMangaRecyclerView()=genreRV.apply {
        mangaGenreAdapter= PreviewMangaListAdapter(R.layout.cell_preview_large,object :PreviewMangaListAdapter.Interaction{
            override fun onErrorRefreshPressed() {
                mangaGenreAdapter.submitLoading()
                setGenreManga()
            }

            override fun onItemSelected(position: Int, item: PreviewMangaResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_genreDialog_to_mangaInfo, bundle)
                this@ListDialogFragment.dismiss()
            }
        })
        val gridLayoutManager=GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (mangaGenreAdapter.getItemViewType(position)==mangaGenreAdapter.ITEM_HOLDER) 1 else 2
            }
        }
        layoutManager=gridLayoutManager
        adapter=mangaGenreAdapter
    }

    private fun setupAnimeRecyclerView()=genreRV.apply {
        animeGenreAdapter=PreviewAnimeListAdapter (R.layout.cell_preview_large,object :PreviewAnimeListAdapter.Interaction{
            override fun onErrorRefreshPressed() {
                animeGenreAdapter.submitLoading()
                if (dialogType==DialogType.GENRE) setGenreAnime()
                else setProducerAnime()
            }

            override fun onItemSelected(position: Int, item: PreviewAnimeResponse) {
                val bundle = bundleOf("mal_id" to item.mal_id)
                navController.navigate(R.id.action_genreDialog_to_animeInfo, bundle)
                this@ListDialogFragment.dismiss()
            }
        })

        val gridLayoutManager=GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (animeGenreAdapter.getItemViewType(position)==animeGenreAdapter.ITEM_HOLDER) 1 else 2
            }
        }
        layoutManager=gridLayoutManager
        adapter=animeGenreAdapter
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main){
            if (dataType==MANGA) mangaGenreAdapter.submitError(message)
            else animeGenreAdapter.submitError(message)
        }
    }

    override fun onDestroyView() {
        genreRV.adapter=null
        super.onDestroyView()
    }
}
