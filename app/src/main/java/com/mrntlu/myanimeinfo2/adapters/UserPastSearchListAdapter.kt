package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import kotlinx.android.synthetic.main.cell_no_item.view.*

class UserPastSearchListAdapter(override val interaction: Interaction<String>? = null) : BaseAdapter<String>() {

    override var errorMessage="Nothing searched before :("

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            else-> UserPastSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_related, parent, false), interaction)
        }
    }

    class UserPastSearchViewHolder constructor(itemView:View,private val interaction: Interaction<String>?):ItemHolder<String>(itemView){
        override fun bind(item:String){
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }
        }
    }
}