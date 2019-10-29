package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder

class UserPastSearchListAdapter(private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var userPastSearchList:ArrayList<String> = arrayListOf()

    private val NO_ITEM_HOLDER=0
    private val ITEM_HOLDER=1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            else-> UserPastSearchViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_related, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is UserPastSearchViewHolder->{
                holder.bind(userPastSearchList[position])
            }
        }
    }

    override fun getItemViewType(position: Int)=if (userPastSearchList.size==0) NO_ITEM_HOLDER else ITEM_HOLDER

    override fun getItemCount()=if (userPastSearchList.size==0) 1 else userPastSearchList.size

    class UserPastSearchViewHolder constructor(itemView:View,private val interaction: Interaction?):RecyclerView.ViewHolder(itemView){
        fun bind(item:String){
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }

        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: String)
    }
}