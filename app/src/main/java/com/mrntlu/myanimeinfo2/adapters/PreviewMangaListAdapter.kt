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

class PreviewMangaListAdapter(private val layout:Int=R.layout.cell_preview,private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //Conditions
    private var isAdapterSet:Boolean=false
    private var isErrorOccured=false
    private var isPaginationLoading=false
    //Holders
    private val LOADING_ITEM_HOLDER=0
    val PREVIEW_HOLDER=1
    private val ERROR_HOLDER=2
    private val NO_ITEM_HOLDER=3
    private val PAGINATION_LOADING_HOLDER=4

    private var errorMessage="Error!"
    private var previewMangaList:ArrayList<PreviewMangaResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            PAGINATION_LOADING_HOLDER-> PaginationLoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_pagination_loading,parent,false))
            else-> PreviewMangaHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreviewMangaHolder -> {
                holder.bind(previewMangaList[position])
            }
            is ErrorItemViewHolder->{
                holder.itemView.errorText.text=errorMessage

                holder.itemView.errorRefreshButton.setOnClickListener {
                    interaction?.onErrorRefreshPressed()
                }
            }
        }
    }

    override fun getItemCount()=if (isAdapterSet && !isErrorOccured && previewMangaList.size!=0 && !isPaginationLoading) previewMangaList.size
    else if (isAdapterSet && !isErrorOccured && isPaginationLoading) previewMangaList.size+1
    else 1

    override fun getItemViewType(position: Int)=if (isAdapterSet){
        when{
            isErrorOccured->ERROR_HOLDER
            previewMangaList.size==0->NO_ITEM_HOLDER
            isPaginationLoading && position==previewMangaList.size->PAGINATION_LOADING_HOLDER
            else->PREVIEW_HOLDER
        }
    }else LOADING_ITEM_HOLDER

    fun submitList(list: List<PreviewMangaResponse>) {
        previewMangaList.apply {
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

    //Pagination
    fun submitPaginationLoading(){
        isPaginationLoading=true
        notifyDataSetChanged()
    }

    fun submitPaginationError(){
        isPaginationLoading=false
        notifyDataSetChanged()
    }

    fun submitPaginationList(list:List<PreviewMangaResponse>){
        isPaginationLoading=false
        previewMangaList.addAll(list)
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

        fun onErrorRefreshPressed()
    }
}