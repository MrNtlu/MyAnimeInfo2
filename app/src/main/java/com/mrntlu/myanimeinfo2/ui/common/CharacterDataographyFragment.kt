package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.CharacterListAdapter
import com.mrntlu.myanimeinfo2.models.CharacterBodyResponse
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.utils.printLog
import kotlinx.android.synthetic.main.fragment_recyclerview.*

class CharacterDataographyFragment(private val dataList:List<CharacterBodyResponse>,private val dataType: DataType) : Fragment() {

    private lateinit var navController: NavController
    private lateinit var listAdapter: CharacterListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView()=fragmentRV.apply {
        layoutManager= LinearLayoutManager(this.context)
        listAdapter= CharacterListAdapter(object :CharacterListAdapter.Interaction{
            override fun onItemSelected(position: Int, item: CharacterBodyResponse) {
                val bundle= bundleOf("mal_id" to item.mal_id)
                navController.navigate(if (dataType== DataType.MANGA) R.id.action_characterInfo_to_mangaInfo else R.id.action_characterInfo_to_animeInfo,bundle)
            }
        })
        listAdapter.submitList(dataList)
        adapter=listAdapter
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
