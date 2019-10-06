package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.models.PreviewMangaResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_preview.view.*

class PreviewMangaListAdapter(private val layout:Int=R.layout.cell_preview,private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAdapterSet:Boolean=false
    val LOADING_ITEM_HOLDER=0
    private val PREVIEW_HOLDER=1
    private var previewMangaList:ArrayList<PreviewMangaResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            else-> PreviewMangaHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreviewMangaHolder -> {
                holder.bind(previewMangaList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int)=if (isAdapterSet) PREVIEW_HOLDER else LOADING_ITEM_HOLDER

    override fun getItemCount()=if (isAdapterSet) previewMangaList.size else 1

    fun submitList(list: List<PreviewMangaResponse>) {
        previewMangaList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet=true
        notifyDataSetChanged()
    }

    class PreviewMangaHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PreviewMangaResponse) = with(itemView) {
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

    interface Interaction {
        fun onItemSelected(position: Int, item: PreviewMangaResponse)
    }
}