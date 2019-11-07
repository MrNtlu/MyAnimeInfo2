package com.mrntlu.myanimeinfo2.ui.anime

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.RelatedListAdapter
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.DialogType
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class AnimeProducersFragment(private val producers:List<GeneralShortResponse> ) : Fragment() {

    private lateinit var producersAdapter:RelatedListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        fragmentRV.apply {
            layoutManager=LinearLayoutManager(context)
            producersAdapter= RelatedListAdapter(object : Interaction<GeneralShortResponse>{
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    val bundle = bundleOf(
                        "genre_name" to item.name,
                        "data_type" to DataType.ANIME.code,
                        "dialog_type" to DialogType.PRODUCER.code,
                        "mal_id" to item.mal_id)
                    navController.navigate(R.id.action_animeInfo_to_genreDialog, bundle)
                }
                override fun onErrorRefreshPressed() {}
            })
            producersAdapter.submitList(producers)
            adapter=producersAdapter
        }
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
