package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.RecommendationsBodyResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import kotlinx.android.synthetic.main.cell_character.view.*

class RecommendationListAdapter(override val interaction: Interaction<RecommendationsBodyResponse>? = null):BaseAdapter<RecommendationsBodyResponse>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER->ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            else->RecommendationHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_character, parent, false), interaction)
        }
    }

    class RecommendationHolder constructor(itemView: View, private val interaction: Interaction<RecommendationsBodyResponse>?) : ItemHolder<RecommendationsBodyResponse>(itemView) {
        override fun bind(item: RecommendationsBodyResponse):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.characterNameText.text=item.title
            val recommendationCount="+${item.recommendation_count}"
            itemView.characterRoleText.text=recommendationCount
            itemView.characterRoleText.setTextColor(resources.getColor(R.color.green900,context.theme))
            itemView.characterImage.loadWithGlide(item.image_url,itemView.characterProgressBar)
        }
    }
}