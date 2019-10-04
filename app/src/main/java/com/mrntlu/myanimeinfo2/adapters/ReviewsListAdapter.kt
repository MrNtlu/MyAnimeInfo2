package com.mrntlu.myanimeinfo2.adapters

import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.ReviewsBodyResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_reviews.view.*

class ReviewsListAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isAdapterSet:Boolean=false
    private val LOADING_ITEM_HOLDER=0
    private val REVIEW_HOLDER=1
    private var reviewsList:ArrayList<ReviewsBodyResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReviewsHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cell_reviews, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReviewsHolder -> {
                holder.bind(reviewsList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }

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

            Glide.with(itemView.context).load(item.reviewer.image_url).addListener(object:
                RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    //todo progressbar
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    return false
                }
            }).into(itemView.userImage)
        }
    }
}