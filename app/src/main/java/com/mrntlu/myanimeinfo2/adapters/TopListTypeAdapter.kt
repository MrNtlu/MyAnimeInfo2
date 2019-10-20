package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.utils.makeCapital
import kotlinx.android.synthetic.main.cell_toplist_type.view.*
import kotlin.collections.ArrayList

class TopListTypeAdapter(private val typeList:ArrayList<String>,private val interaction:Interaction?=null):RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TopListTypeHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_toplist_type,parent,false),interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is TopListTypeHolder-> {
                holder.bind(typeList[position])
            }
        }
    }

    override fun getItemCount()=typeList.size

    class TopListTypeHolder constructor(itemView:View, private val interaction:Interaction?):RecyclerView.ViewHolder(itemView){
        fun bind(item:String)= with(itemView){
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition,item)
            }

            itemView.typeText.text=item.makeCapital()
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: String)
    }
}