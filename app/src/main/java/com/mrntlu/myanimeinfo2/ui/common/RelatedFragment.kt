package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.RelatedListAdapter
import com.mrntlu.myanimeinfo2.adapters.RelatedListAdapter.*
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import com.mrntlu.myanimeinfo2.models.RelatedResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.fragment_related.*

class RelatedFragment(private val relatedResponse: RelatedResponse,private val dataType: DataType) : Fragment() {

    private lateinit var adaptationAdapter: RelatedListAdapter
    private lateinit var prequelAdapter: RelatedListAdapter
    private lateinit var sequelAdapter: RelatedListAdapter
    private lateinit var sideStoryAdapter: RelatedListAdapter
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_related, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupRecyclerView()
        setListeners()
    }

    private fun setListeners() {
        if (relatedResponse.Adaptation!=null) setVisiblity(adaptationsLayout,adaptationsRV,adaptationExpandImage)
        if (relatedResponse.Prequel!=null) setVisiblity(prequelsLayout,prequelsRV,prequelExpandImage)
        if (relatedResponse.Sequel!=null) setVisiblity(sequelsLayout,sequelsRV,sequelExpandImage)
        if (relatedResponse.Side!=null) setVisiblity(sideStoryLayout,sideStoriesRV,sideStoryExpandImage)
    }

    private fun setVisiblity(view:View,recyclerView: RecyclerView,imageView: ImageView){
        view.setOnClickListener {
            if (recyclerView.isVisible) recyclerView.setGone()
            else recyclerView.setVisible()
            imageView.setImageResource(if (recyclerView.isVisible) R.drawable.ic_arrow_up_24dp else R.drawable.ic_arrow_down_24dp)
        }
    }

    private fun setupRecyclerView() {
        printLog(message = relatedResponse.toString())

        if (relatedResponse.Adaptation==null) adaptationsLayout.setGone()
        else adaptationsRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            adaptationAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    printLog(message = "Item ${item.mal_id} ${item.name}")
                    if (dataType==DataType.MANGA) navigateWithBundle(item.mal_id,R.id.action_mangaInfo_to_animeInfo)
                    else navigateWithBundle(item.mal_id,R.id.action_animeInfo_to_mangaInfo)
                }
            })
            adaptationAdapter.submitList(relatedResponse.Adaptation)
            adapter=adaptationAdapter
        }

        if (relatedResponse.Prequel==null) prequelsLayout.setGone()
        else prequelsRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            prequelAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    if (dataType==DataType.ANIME) navigateWithBundle(item.mal_id,R.id.action_animeInfo_self)
                    else navigateWithBundle(item.mal_id,R.id.action_mangaInfo_self)
                }
            })
            prequelAdapter.submitList(relatedResponse.Prequel)
            adapter=prequelAdapter
        }

        if (relatedResponse.Sequel==null) sequelsLayout.setGone()
        else sequelsRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            sequelAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    if (dataType==DataType.ANIME) navigateWithBundle(item.mal_id,R.id.action_animeInfo_self)
                    else navigateWithBundle(item.mal_id,R.id.action_mangaInfo_self)
                }
            })
            sequelAdapter.submitList(relatedResponse.Sequel)
            adapter=sequelAdapter
        }

        if (relatedResponse.Side==null) sideStoryLayout.setGone()
        else sideStoriesRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            sideStoryAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    if (dataType==DataType.ANIME) navigateWithBundle(item.mal_id,R.id.action_animeInfo_self)
                    else navigateWithBundle(item.mal_id,R.id.action_mangaInfo_self)
                }
            })
            sideStoryAdapter.submitList(relatedResponse.Side)
            adapter=sideStoryAdapter
        }

        if (relatedResponse.Adaptation==null && relatedResponse.Prequel==null && relatedResponse.Sequel==null && relatedResponse.Side==null) noRelationText.setVisible()
        else noRelationText.setGone()
    }

    private fun navigateWithBundle(malID:Int,navID:Int){
        val bundle= bundleOf("mal_id" to malID)
        navController.navigate(navID,bundle)
    }

    override fun onDestroyView() {
        adaptationsRV.adapter=null
        prequelsRV.adapter=null
        sequelsRV.adapter=null
        sideStoriesRV.adapter=null
        super.onDestroyView()
    }
}
