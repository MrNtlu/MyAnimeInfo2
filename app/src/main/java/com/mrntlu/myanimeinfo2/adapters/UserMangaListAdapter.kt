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
import com.mrntlu.myanimeinfo2.models.UserMangaListBody
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_user_list.view.*

class UserMangaListAdapter(override val interaction: Interaction<UserMangaListBody>? = null) : BaseAdapter<UserMangaListBody>(){

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
            else-> MangaListHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_user_list, parent, false), interaction)
        }
    }

    class MangaListHolder constructor(itemView: View, private val interaction: Interaction<UserMangaListBody>?) : ItemHolder<UserMangaListBody>(itemView) {
        override fun bind(item: UserMangaListBody):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.userListView.setBackgroundColor(resources.getColor(
                when(item.reading_status){
                    1->R.color.green500
                    2->R.color.darkBlue
                    3->R.color.yellow500
                    4->R.color.red800
                    else->R.color.grey400
                },itemView.context.theme)
            )

            itemView.userListTitle.text=item.title
            itemView.userListType.text=when(item.reading_status){
                1->"Reading"
                2->"Completed"
                3->"OnHold"
                4->"Dropped"
                else->"Plan to Read"
            }
            itemView.userListScore.text=item.score.toInt().toString()

            val volumeText="${item.read_volumes}/${item.total_volumes}"
            itemView.userListEpisodes.text=volumeText

            val chapterText="${item.read_chapters}/${item.total_chapters}"
            itemView.userListChapters.text=chapterText

            itemView.imageProgress.setVisible()
            itemView.userListImage.loadWithGlide(item.image_url,itemView.imageProgress)
        }
    }
}