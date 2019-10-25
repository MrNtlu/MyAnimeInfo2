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
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.printLog
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.cell_preview.view.*

class PreviewMangaListAdapter(private val layout:Int=R.layout.cell_preview, override val interaction: Interaction<PreviewMangaResponse>? = null) : BaseAdapter<PreviewMangaResponse>(interaction) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            PAGINATION_LOADING_HOLDER-> PaginationLoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_pagination_loading,parent,false))
            else-> PreviewMangaHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), interaction)
        }
    }

    class PreviewMangaHolder constructor(itemView: View, private val interaction: Interaction<PreviewMangaResponse>?) : ItemHolder<PreviewMangaResponse>(itemView) {
        override fun bind(item: PreviewMangaResponse): Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.titleText.text=item.title
            itemView.typeText.text=item.type
            itemView.scoreText.text=item.score.toString()

            val volumesText=if (item.volumes!=null) item.volumes.toString() else "?"
            val chaptersText=if (item.chapters!=null) "/${item.chapters}" else ""
            val text="$volumesText $chaptersText"
            itemView.episodesText.text=text

            itemView.previewImageProgress.setVisible()
            itemView.previewImage.loadWithGlide(item.image_url,itemView.previewImageProgress)
        }
    }
}