package com.mrntlu.myanimeinfo2.ui.others

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.UserFavoritesListAdapter
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.UserShortResponse
import kotlinx.android.synthetic.main.fragment_recyclerview.*

enum class ProfileFavoriteType(val code:Int) {
    ANIME(0),
    MANGA(1),
    CHARACTERS(2),
    PEOPLE(3);

    companion object{
        private val values = values()
        fun getByCode(code: Int) = values.first { it.code == code }
    }
}
class UserProfileFavoritesFragment(private val userShortResponse: List<UserShortResponse>,private val favoriteType: ProfileFavoriteType) : Fragment() {

    private lateinit var navController: NavController
    private lateinit var userListAdapter: UserFavoritesListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController= Navigation.findNavController(view)

        setupRecyclerView()
    }

    private fun setupRecyclerView()=fragmentRV.apply {
        val gridLayoutManager=GridLayoutManager(context,2)
        gridLayoutManager.spanSizeLookup=object: GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (userListAdapter.getItemViewType(position)==userListAdapter.ITEM_HOLDER) 1 else 2
            }
        }
        layoutManager=gridLayoutManager
        userListAdapter= UserFavoritesListAdapter(object :Interaction<UserShortResponse>{
            override fun onItemSelected(position: Int, item: UserShortResponse) {
                when (favoriteType) {
                    ProfileFavoriteType.MANGA -> navigateWithBundle(R.id.action_userProfile_to_mangaInfo,item.mal_id)
                    ProfileFavoriteType.ANIME -> navigateWithBundle(R.id.action_userProfile_to_animeInfo,item.mal_id)
                    ProfileFavoriteType.CHARACTERS -> navigateWithBundle(R.id.action_userProfile_to_characterInfo,item.mal_id)
                    else ->{}
                }
            }
            override fun onErrorRefreshPressed(){}
        })
        userListAdapter.submitList(userShortResponse)
        adapter=userListAdapter
    }

    private fun navigateWithBundle(destination:Int,malID:Int){
        val bundle= bundleOf("mal_id" to malID)
        navController.navigate(destination,bundle)
    }

    override fun onDestroyView() {
        fragmentRV.adapter=null
        super.onDestroyView()
    }
}
