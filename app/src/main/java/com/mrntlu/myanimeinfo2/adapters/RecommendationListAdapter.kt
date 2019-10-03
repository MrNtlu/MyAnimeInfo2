package com.mrntlu.myanimeinfo2.adapters

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.models.RecommendationsBodyResponse
import kotlinx.android.synthetic.main.cell_character.view.*

class RecommendationListAdapter(private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAdapterSet:Boolean=false
    private val LOADING_ITEM_HOLDER=0
    private val RECOMMENDATION_HOLDER=1
    private var recommendationList:ArrayList<RecommendationsBodyResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER->{
                LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            }
            else->RecommendationHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_character, parent, false), interaction)
        }    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecommendationHolder -> {
                holder.bind(recommendationList.get(position))
            }
        }
    }

    override fun getItemCount() = recommendationList.size

    override fun getItemViewType(position: Int)=if (isAdapterSet) RECOMMENDATION_HOLDER else LOADING_ITEM_HOLDER

    fun submitList(list: List<RecommendationsBodyResponse>) {
        recommendationList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet=true
        notifyDataSetChanged()
    }

    class RecommendationHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: RecommendationsBodyResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.characterNameText.text=item.title
            itemView.characterRoleText.text=item.recommendation_count.toString()

            Glide.with(itemView.context).load(item.image_url).addListener(object:
                RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    //todo progressbar
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }

            }).into(itemView.characterImage)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: RecommendationsBodyResponse)
    }
}