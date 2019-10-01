package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.RelatedListAdapter
import com.mrntlu.myanimeinfo2.adapters.RelatedListAdapter.*
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import com.mrntlu.myanimeinfo2.models.RelatedResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.fragment_related.*

class RelatedFragment(private val relatedResponse: RelatedResponse) : Fragment() {


    private lateinit var adaptationAdapter: RelatedListAdapter
    private lateinit var prequelAdapter: RelatedListAdapter
    private lateinit var sequelAdapter: RelatedListAdapter
    private lateinit var sideStoryAdapter: RelatedListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_related, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setListeners(view)
    }

    private fun setListeners(view: View) {

    }

    private fun setupRecyclerView() {
        if (relatedResponse.Adaptation==null) adaptationsLayout.setGone()
        else adaptationsRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            adaptationAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    printLog(message = "Item ${item.mal_id} ${item.name}")
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
                    printLog(message = "Item ${item.mal_id} ${item.name}")
                }
            })
            adaptationAdapter.submitList(relatedResponse.Prequel)
            adapter=prequelAdapter
        }

        if (relatedResponse.Sequel==null) sequelsLayout.setGone()
        else sequelsRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            sequelAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    printLog(message = "Item ${item.mal_id} ${item.name}")
                }
            })
            adaptationAdapter.submitList(relatedResponse.Sequel)
            adapter=sequelAdapter
        }

        if (relatedResponse.Side==null) sideStoryLayout.setGone()
        else sideStoriesRV.apply {
            layoutManager= LinearLayoutManager(this.context)
            sideStoryAdapter= RelatedListAdapter(object : Interaction {
                override fun onItemSelected(position: Int, item: GeneralShortResponse) {
                    printLog(message = "Item ${item.mal_id} ${item.name}")
                }
            })
            adaptationAdapter.submitList(relatedResponse.Side)
            adapter=sideStoryAdapter
        }

        if (relatedResponse.Adaptation==null && relatedResponse.Prequel==null && relatedResponse.Sequel==null && relatedResponse.Side==null) noRelationText.setVisible()
        else noRelationText.setGone()
    }
}
