package com.mrntlu.myanimeinfo2.ui.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.pageradapters.PicturesPagerAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.models.PictureBodyResponse
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import kotlinx.android.synthetic.main.fragment_pictures.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates

class PicturesFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var commonViewModel: CommonViewModel
    private var malID by Delegates.notNull<Int>()
    private lateinit var navController: NavController
    private lateinit var dataType: DataType
    private lateinit var pictures:List<PictureBodyResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            malID=it.getInt("mal_id")
            dataType=DataType.getByCode(it.getInt("data_type"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pictures, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        commonViewModel= ViewModelProviders.of(this).get(CommonViewModel::class.java)

        setupObservers()
    }

    private fun setListeners(){
        downloadFab.setOnClickListener {
            printLog(message = pictures[pictureViewpager.currentItem].toString())
        }
    }

    private fun setupObservers() {
        commonViewModel.getPicturesByID(dataType.toString().toLowerCase(Locale.ENGLISH),malID,this).observe(viewLifecycleOwner, Observer {
            pictures=it.pictures
            setupViewPagers(it.pictures)
            setListeners()
        })
    }

    private fun setupViewPagers(pictures: List<PictureBodyResponse>) {
        val fragments= arrayListOf<Fragment>()
        for (picture in pictures){
            fragments.add(PictureItemFragment(picture))
        }
        val pagerAdapter=PicturesPagerAdapter(childFragmentManager,fragments)
        pictureViewpager.adapter=pagerAdapter
        pictureTabLayout.setupWithViewPager(pictureViewpager,true)
    }

    override fun onError(message: String) {
        printLog(message=message)
    }

    override fun onDestroyView() {
        pictureViewpager.adapter=null
        super.onDestroyView()
    }
}
