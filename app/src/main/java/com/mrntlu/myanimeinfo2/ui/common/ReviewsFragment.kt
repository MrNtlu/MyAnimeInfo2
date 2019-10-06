package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.ReviewsListAdapter
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.fragment_recyclerview.*
import java.util.*

class ReviewsFragment(private val malID:Int,private val dataType:DataType) : Fragment() {

    private lateinit var commonViewModel: CommonViewModel
    private lateinit var reviewsListAdapter:ReviewsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        commonViewModel=ViewModelProviders.of(this).get(CommonViewModel::class.java)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        commonViewModel.getReviewsByID(dataType.name.toLowerCase(Locale.ENGLISH),malID,1).observe(viewLifecycleOwner,androidx.lifecycle.Observer {
            reviewsListAdapter.submitList(it.reviews)
        })
    }

    private fun setupRecyclerView() =fragmentRV.apply {
        layoutManager=LinearLayoutManager(this.context)
        reviewsListAdapter= ReviewsListAdapter()
        adapter=reviewsListAdapter
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
