package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.models.GeneralShortResponse
import kotlinx.android.synthetic.main.cell_genre_tags.view.*

class GenreTagListAdapter(private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var genreTagList:ArrayList<GeneralShortResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GenreTagHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_genre_tags, parent, false), interaction)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is GenreTagHolder -> {
                holder.bind(genreTagList[position])
            }
        }
    }

    override fun getItemCount()=genreTagList.size

    fun submitList(list: List<GeneralShortResponse>) {
        genreTagList.apply {
            this.clear()
            this.addAll(list)
        }
        notifyDataSetChanged()
    }

    class GenreTagHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: GeneralShortResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.genreTagText.text=item.name
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: GeneralShortResponse)
    }
}