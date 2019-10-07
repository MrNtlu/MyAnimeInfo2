package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.models.ReviewsBodyResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_reviews.view.*

class ReviewsListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAdapterSet:Boolean=false
    private val LOADING_ITEM_HOLDER=0
    private val REVIEW_HOLDER=1
    private var reviewsList:ArrayList<ReviewsBodyResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            else->ReviewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_reviews, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReviewsHolder -> {
                holder.bind(reviewsList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int)=if (isAdapterSet) REVIEW_HOLDER else LOADING_ITEM_HOLDER

    override fun getItemCount()=if (isAdapterSet) reviewsList.size else 1

    fun submitList(list: List<ReviewsBodyResponse>) {
        reviewsList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet=true
        notifyDataSetChanged()
    }

    class ReviewsHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ReviewsBodyResponse) = with(itemView) {
            itemView.setOnClickListener {
                if (itemView.reviewText.isVisible){
                    itemView.reviewText.setGone()
                    itemView.expandImage.setImageResource(R.drawable.ic_arrow_down_24dp)
                }else{
                    itemView.reviewText.setVisible()
                    itemView.expandImage.setImageResource(R.drawable.ic_arrow_up_24dp)
                }
            }

            itemView.overallScore.text=item.reviewer.scores.overall.toString()
            itemView.storyScore.text=item.reviewer.scores.story.toString()
            itemView.soundScore.text=item.reviewer.scores.sound.toString()
            itemView.enjoymentScore.text=item.reviewer.scores.enjoyment.toString()
            itemView.characterScore.text=item.reviewer.scores.character.toString()
            itemView.animationScore.text=item.reviewer.scores.animation.toString()
            itemView.usernameText.text=item.reviewer.username
            itemView.reviewText.text=item.content
            itemView.userImage.loadWithGlide(item.reviewer.image_url,itemView.reviewProgressBar)
        }
    }
}