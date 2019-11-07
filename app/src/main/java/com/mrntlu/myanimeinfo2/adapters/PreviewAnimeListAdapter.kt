package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.PaginationLoadingViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_preview.view.*

class PreviewAnimeListAdapter(private val layout:Int=R.layout.cell_preview, override val interaction: Interaction<PreviewAnimeResponse>? = null) : BaseAdapter<PreviewAnimeResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            PAGINATION_LOADING_HOLDER-> PaginationLoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_pagination_loading,parent,false))
            else-> PreviewAnimeHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), interaction)
        }
    }

    class PreviewAnimeHolder constructor(itemView: View, private val interaction: Interaction<PreviewAnimeResponse>?) : ItemHolder<PreviewAnimeResponse>(itemView) {
        override fun bind(item: PreviewAnimeResponse):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.titleText.text=item.title
            itemView.typeText.text=item.type
            itemView.scoreText.text=item.score.toString()
            itemView.episodesText.text=if (item.episodes!=null) item.episodes.toString() else "?"

            itemView.previewImageProgress.setVisible()
            itemView.previewImage.loadWithGlide(item.image_url,itemView.previewImageProgress)
        }
    }
}