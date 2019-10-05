package com.mrntlu.myanimeinfo2.ui.manga

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
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.MangaResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_manga_info.*
import kotlin.properties.Delegates

class MangaInfoFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var mangaViewModel: MangaViewModel
    private var malID by Delegates.notNull<Int>()
    private lateinit var mangaResponse:MangaResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            malID=it.getInt("mal_id")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manga_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        mangaViewModel = ViewModelProviders.of(view.context as AppCompatActivity).get(MangaViewModel::class.java)

        setupObservers()
        genreButton.setOnClickListener {
            //val fragmentDialog=GenreDialogFragment(mangaResponse.genres!![2].mal_id,0)
            //fragmentDialog.show(childFragmentManager,"dialog")
            val bundle= bundleOf("genre_name" to mangaResponse.genres!![2].name,"data_type" to DataType.MANGA.code, "mal_id" to mangaResponse.genres!![2].mal_id)
            navController.navigate(R.id.action_mangaInfo_to_genreDialog,bundle)
        }
    }

    private fun setupObservers()=mangaViewModel.getMangaByID(malID).observe(viewLifecycleOwner, Observer {
        printLog(message = it.toString())
        mangaResponse=it
    })

}
