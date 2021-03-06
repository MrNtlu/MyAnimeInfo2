package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.ReviewsBodyResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_reviews.view.*

class ReviewsListAdapter(override val interaction: Interaction<ReviewsBodyResponse>? = null) : BaseAdapter<ReviewsBodyResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            else->ReviewsHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_reviews, parent, false))
        }
    }

    class ReviewsHolder constructor(itemView: View) : ItemHolder<ReviewsBodyResponse>(itemView) {
        override fun bind(item: ReviewsBodyResponse):Unit = with(itemView) {
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