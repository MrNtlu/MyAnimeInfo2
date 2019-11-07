package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.PaginationLoadingViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction

import com.mrntlu.myanimeinfo2.models.UserShortResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_preview_large.view.*

class UserFavoritesListAdapter(override val interaction: Interaction<UserShortResponse>? = null) : BaseAdapter<UserShortResponse>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            PAGINATION_LOADING_HOLDER-> PaginationLoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_pagination_loading,parent,false))
            else-> ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_preview_large, parent, false), interaction)
        }
    }

    class ItemViewHolder constructor(itemView: View, private val interaction: Interaction<UserShortResponse>?) : ItemHolder<UserShortResponse>(itemView) {
        override fun bind(item: UserShortResponse):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.titleText.text=item.name
            itemView.typeText.setGone()
            itemView.scoreText.setGone()
            itemView.episodesText.setGone()

            itemView.previewImageProgress.setVisible()
            itemView.previewImage.loadWithGlide(item.image_url,itemView.previewImageProgress)
        }
    }
}