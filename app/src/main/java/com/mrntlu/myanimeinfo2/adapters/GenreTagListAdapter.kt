package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import kotlinx.android.synthetic.main.cell_genre_tags.view.*

class GenreTagListAdapter(override val interaction: Interaction<GeneralShortResponse>? = null) : BaseAdapter<GeneralShortResponse>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenreTagHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_genre_tags, parent, false), interaction)
    }

    class GenreTagHolder constructor(itemView: View, private val interaction: Interaction<GeneralShortResponse>?) : ItemHolder<GeneralShortResponse>(itemView) {
        override fun bind(item: GeneralShortResponse):Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.genreTagText.text=item.name
        }
    }
}