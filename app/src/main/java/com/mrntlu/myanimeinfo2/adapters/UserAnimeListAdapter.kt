package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.NoItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.PaginationLoadingViewHolder
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.UserAnimeListBody
import com.mrntlu.myanimeinfo2.utils.*
import kotlinx.android.synthetic.main.cell_user_list.view.*

class UserAnimeListAdapter(override val interaction: Interaction<UserAnimeListBody>? = null) : BaseAdapter<UserAnimeListBody>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            NO_ITEM_HOLDER-> NoItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_no_item,parent,false))
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            PAGINATION_LOADING_HOLDER-> PaginationLoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.cell_pagination_loading,parent,false))
            else-> AnimeListHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_user_list, parent, false), interaction)
        }
    }

    class AnimeListHolder constructor(itemView: View, private val interaction: Interaction<UserAnimeListBody>?) : ItemHolder<UserAnimeListBody>(itemView) {
        override fun bind(item: UserAnimeListBody):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            printLog(message = item.watching_status.toString())

            itemView.userListView.setBackgroundColor(resources.getColor(
                when(item.watching_status){
                    1->R.color.green500
                    2->R.color.darkBlue
                    3->R.color.yellow500
                    4->R.color.red800
                    else->R.color.grey400
                },itemView.context.theme)
            )

            itemView.userListTitle.text=item.title
            itemView.userListType.text=when(item.watching_status){
                1->"Watching"
                2->"Completed"
                3->"OnHold"
                4->"Dropped"
                else->"Plan to Watch"
            }

            itemView.userListScore.text=item.score.toInt().toString()

            itemView.userListVolumesText.text="Episodes:"
            val volumeText="${item.watched_episodes}/${item.total_episodes}"
            itemView.userListEpisodes.text=volumeText

            itemView.userListChaptersText.setGone()
            itemView.userListChapters.setGone()

            itemView.imageProgress.setVisible()
            itemView.userListImage.loadWithGlide(item.image_url,itemView.imageProgress)
        }
    }
}