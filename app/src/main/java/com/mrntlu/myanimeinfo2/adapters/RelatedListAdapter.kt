package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import kotlinx.android.synthetic.main.cell_related.view.*

class RelatedListAdapter(override val interaction: Interaction<GeneralShortResponse>? = null) : BaseAdapter<GeneralShortResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            else-> RelatedHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_related, parent, false), interaction)
        }
    }

    class RelatedHolder constructor(itemView: View, private val interaction: Interaction<GeneralShortResponse>?) : ItemHolder<GeneralShortResponse>(itemView) {
        override fun bind(item: GeneralShortResponse):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.relatedDataText.text=item.name
        }
    }
}