package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import kotlinx.android.synthetic.main.cell_related.view.*

class RelatedListAdapter(private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val NO_ITEM_HOLDER=0
    private val ITEM_HOLDER=1
    private var isAdapterSet:Boolean=false
    private var relatedList:ArrayList<GeneralShortResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            else-> RelatedHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_related, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RelatedHolder -> {
                holder.bind(relatedList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int)=if (relatedList.size==0) NO_ITEM_HOLDER else ITEM_HOLDER

    override fun getItemCount()=relatedList.size

    fun submitList(list: List<GeneralShortResponse>) {
        relatedList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet=true
        notifyDataSetChanged()
    }

    class RelatedHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: GeneralShortResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.relatedDataText.text=item.name
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: GeneralShortResponse)
    }
}