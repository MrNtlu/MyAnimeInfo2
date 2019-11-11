package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.DataType
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.showToast
import com.mrntlu.myanimeinfo2.viewmodels.AnimeViewModel
import com.mrntlu.myanimeinfo2.viewmodels.CommonViewModel
import com.mrntlu.myanimeinfo2.viewmodels.MangaViewModel
import kotlinx.android.synthetic.main.fragment_anime_season.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserListFragment : Fragment(), CoroutinesErrorHandler {

    private lateinit var navController: NavController
    private lateinit var commonViewModel: CommonViewModel
    private lateinit var username:String
    private lateinit var dataType: DataType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            username=it.getString("username","")
            dataType= DataType.getByCode(it.getInt("data_type"))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_anime_season, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        commonViewModel= ViewModelProviders.of(this).get(CommonViewModel::class.java)
        seasonSpinner.setGone()

        setupObservers()
    }

    private fun setupObservers() {
        if (dataType==DataType.ANIME){
            commonViewModel.getUserAnimeList(username,this).observe(viewLifecycleOwner, Observer {

            })
        }else{
            commonViewModel.getUserMangaList(username,this).observe(viewLifecycleOwner, Observer {

            })
        }
    }

    override fun onError(message: String) {
        GlobalScope.launch(Dispatchers.Main){

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
