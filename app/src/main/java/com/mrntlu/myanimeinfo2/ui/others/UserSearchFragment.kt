package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager

import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.UserPastSearchListAdapter
import com.mrntlu.myanimeinfo2.utils.hideKeyboard
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import com.mrntlu.myanimeinfo2.utils.showToast
import kotlinx.android.synthetic.main.fragment_search_user.*

class UserSearchFragment : Fragment(), UserPastSearchListAdapter.Interaction {

    private lateinit var navController: NavController
    private lateinit var userPastListAdapter:UserPastSearchListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setSearchView()
        setupRecyclerView()
        setListeners()
    }

    private fun setListeners() {
        userSearchCardView.setOnClickListener {
            if (userSearchCautionText.isVisible){
                userSearchCautionText.setGone()
                userSearchExpandButton.setImageResource(R.drawable.ic_arrow_down_24dp)
            }else{
                userSearchCautionText.setVisible()
                userSearchExpandButton.setImageResource(R.drawable.ic_arrow_up_24dp)
            }
        }
    }

    private fun setupRecyclerView()=pastSearchRV.apply {
        layoutManager= LinearLayoutManager(context)
        userPastListAdapter=UserPastSearchListAdapter(this@UserSearchFragment)
        adapter=userPastListAdapter
    }

    private fun setSearchView()=userSearchView.apply {
        this.isIconified=false

        this.setOnCloseListener { true }

        this.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?):Boolean{
                if (query!=null && !query.isBlank()) {
                    this@apply.clearFocus()
                    navigateWithBundle(query)
                }else showToast(this@apply.context,"Please type something.")
                return false
            }
            override fun onQueryTextChange(newText: String?)=true
        })
    }

    private fun navigateWithBundle(username:String){
        val bundle = bundleOf("username" to username)
        navController.navigate(R.id.action_userSearch_to_userProfile,bundle)
    }

    override fun onItemSelected(position: Int, item: String) {
        navigateWithBundle(item)
    }

    override fun onResume() {
        super.onResume()
        userSearchView.clearFocus()
    }

    override fun onDestroyView() {
        pastSearchRV.adapter=null
        view?.hideKeyboard()
        userSearchView.setOnCloseListener(null)
        userSearchView.setOnQueryTextListener(null)
        super.onDestroyView()
    }
}
