package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.UserPastSearchListAdapter
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.UserSearch
import com.mrntlu.myanimeinfo2.utils.*
import com.mrntlu.myanimeinfo2.viewmodels.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search_user.*

class UserSearchFragment : Fragment(), Interaction<UserSearch>, CoroutinesErrorHandler {

    private lateinit var navController: NavController
    private lateinit var userPastListAdapter:UserPastSearchListAdapter
    private lateinit var searchViewModel:SearchViewModel

    private lateinit var userSearchList:List<UserSearch>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)
        searchViewModel= ViewModelProvider(this).get(SearchViewModel::class.java)

        val textArea=userSearchView.findViewById<SearchView.SearchAutoComplete>(R.id.search_src_text)
        textArea.setTextColor(resources.getColor(R.color.materialBlack,view.context.theme))

        setSearchView()
        setupRecyclerView()
        setListeners()
        setupObserver()
    }

    private fun setupObserver() {
        searchViewModel.getSearches().observe(viewLifecycleOwner, Observer {
            userSearchList=it
            userPastListAdapter.submitList(it)
        })
    }
    
    private fun insertSearch(search:UserSearch){
        if (userSearchList.size==10){
            deleteSearch(userSearchList[9])
            val tempList= ArrayList(userSearchList).also { it.removeAt(9) }
            userSearchList= tempList
            insertSearch(search)
        }else {
            searchViewModel.insertSearch(search, this).observe(viewLifecycleOwner, Observer {
                navigateWithBundle(it.search)
            })
        }
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
        userPastListAdapter=UserPastSearchListAdapter(this@UserSearchFragment,::deleteSearch)
        adapter=userPastListAdapter
    }

    private fun deleteSearch(userSearch: UserSearch){
        searchViewModel.deleteSearch(userSearch,this)
    }

    private fun setSearchView()=userSearchView.apply {
        this.isIconified=false

        this.setOnCloseListener { true }

        this.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?):Boolean{
                if (query!=null && !query.isBlank()) {
                    this@apply.clearFocus()
                    insertSearch(UserSearch(search= query))
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

    override fun onErrorRefreshPressed() {}

    override fun onItemSelected(position: Int, item: UserSearch) {
        navigateWithBundle(item.search)
    }

    override fun onError(message: String) {
        userPastListAdapter.submitError(message)
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
