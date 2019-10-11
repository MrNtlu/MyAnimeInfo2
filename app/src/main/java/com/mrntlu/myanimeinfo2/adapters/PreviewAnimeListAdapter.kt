package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.models.PreviewAnimeResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_error.view.*
import kotlinx.android.synthetic.main.cell_preview.view.*

class PreviewAnimeListAdapter(private val layout:Int=R.layout.cell_preview,private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAdapterSet:Boolean=false
    private var isErrorOccured=false
    private val LOADING_ITEM_HOLDER=0
    val PREVIEW_HOLDER=1
    private val ERROR_HOLDER=2
    private var errorMessage="Error!"
    private var previewAnimeList:ArrayList<PreviewAnimeResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            else-> PreviewAnimeHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreviewAnimeHolder -> {
                holder.bind(previewAnimeList.get(position))
            }
            is ErrorItemViewHolder->{
                holder.itemView.errorText.text=errorMessage

                holder.itemView.errorRefreshButton.setOnClickListener {
                    interaction?.onErrorRefreshPressed()
                }
            }
        }
    }

    override fun getItemCount()=if (isAdapterSet) if (isErrorOccured) 1 else previewAnimeList.size else 1

    override fun getItemViewType(position: Int)=if (isAdapterSet){ if (isErrorOccured) ERROR_HOLDER else PREVIEW_HOLDER }else LOADING_ITEM_HOLDER

    fun submitList(list: List<PreviewAnimeResponse>) {
        previewAnimeList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet=true
        isErrorOccured=false
        notifyDataSetChanged()
    }

    fun submitLoading(){
        isAdapterSet=false
        isErrorOccured=false
        notifyDataSetChanged()
    }

    fun submitError(message:String){
        isAdapterSet=true
        isErrorOccured=true
        errorMessage=message
        notifyDataSetChanged()
    }

    class PreviewAnimeHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PreviewAnimeResponse) = with(itemView) {
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

    interface Interaction {
        fun onErrorRefreshPressed()

        fun onItemSelected(position: Int, item: PreviewAnimeResponse)
    }
}