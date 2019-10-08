package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.models.RecommendationsBodyResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import kotlinx.android.synthetic.main.cell_character.view.*
import kotlinx.android.synthetic.main.cell_error.view.*

class RecommendationListAdapter(private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAdapterSet=false
    private var isErrorOccured=false
    private val LOADING_ITEM_HOLDER=0
    private val RECOMMENDATION_HOLDER=1
    private val ERROR_HOLDER=2
    private var errorMessage="Error!"
    private var recommendationList:ArrayList<RecommendationsBodyResponse> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER->ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            else->RecommendationHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_character, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecommendationHolder -> {
                holder.bind(recommendationList[position])
            }
            is ErrorItemViewHolder->{
                holder.itemView.errorText.text=errorMessage

                holder.itemView.errorRefreshButton.setOnClickListener {
                    interaction?.onErrorRefreshPressed()
                }
            }
        }
    }

    override fun getItemCount()=if (isAdapterSet) if (isErrorOccured) 1 else recommendationList.size else 1

    override fun getItemViewType(position: Int)=if (isAdapterSet){ if (isErrorOccured) ERROR_HOLDER else RECOMMENDATION_HOLDER }else LOADING_ITEM_HOLDER

    fun submitList(list: List<RecommendationsBodyResponse>) {
        recommendationList.apply {
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

    class RecommendationHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: RecommendationsBodyResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.characterNameText.text=item.title
            val recommendationCount="+${item.recommendation_count}"
            itemView.characterRoleText.text=recommendationCount
            itemView.characterRoleText.setTextColor(resources.getColor(R.color.green900,this.context.theme))
            itemView.characterImage.loadWithGlide(item.image_url,itemView.characterProgressBar)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RecommendationsBodyResponse)

        fun onErrorRefreshPressed()
    }
}