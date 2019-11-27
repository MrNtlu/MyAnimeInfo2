package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.RecommendationListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.RecommendationsBodyResponse
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class RecommendationFragment(private val dataType: DataType,private val malID:Int) : Fragment(), CoroutinesErrorHandler {
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var recommendationListAdapter:RecommendationListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        commonViewModel = ViewModelProviders.of(this).get(CommonViewModel::class.java)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        commonViewModel.getRecommendationsByID(dataType.name.toLowerCase(Locale.ENGLISH),malID,this).observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            recommendationListAdapter.submitList(it.recommendations)
        })
    }

    private fun setupRecyclerView()=fragmentRV.apply {
        layoutManager= LinearLayoutManager(context)
        recommendationListAdapter= RecommendationListAdapter(object : Interaction<RecommendationsBodyResponse> {
            override fun onErrorRefreshPressed() {
                recommendationListAdapter.submitLoading()
                setupObservers()
            }

            override fun onItemSelected(position: Int, item: RecommendationsBodyResponse) {
                val bundle= bundleOf("mal_id" to item.mal_id)
                navController.navigate(if (dataType==DataType.ANIME) R.id.action_animeInfo_self else R.id.action_mangaInfo_self,bundle)
            }
        })
        adapter=recommendationListAdapter
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main) {
            recommendationListAdapter.submitError(message)
        }
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
