package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.UserSearch
import kotlinx.android.synthetic.main.cell_no_item.view.*
import kotlinx.android.synthetic.main.cell_user_search.view.*

class UserPastSearchListAdapter constructor(override val interaction: Interaction<UserSearch>? = null, private val onDelete: (userSearch: UserSearch) -> Unit) : BaseAdapter<UserSearch>() {

    override var errorMessage="Nothing searched before :("

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            else-> UserPastSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_user_search, parent, false), interaction,onDelete)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (getItemViewType(position)==NO_ITEM_HOLDER){
            holder.itemView.cellNoItem.text=holder.itemView.context.resources.getString(R.string.no_past_search_found_)
        }
    }

    class UserPastSearchViewHolder constructor(itemView:View,private val interaction: Interaction<UserSearch>?,val onDelete:(userSearch:UserSearch)->Unit):ItemHolder<UserSearch>(itemView){
        override fun bind(item:UserSearch){
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }
            itemView.userSearchDeleteButton.setOnClickListener {
                onDelete(item)
            }
            itemView.userSearchText.text=item.search
        }
    }
}