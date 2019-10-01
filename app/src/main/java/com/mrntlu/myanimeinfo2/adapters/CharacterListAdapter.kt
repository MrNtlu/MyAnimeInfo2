package com.mrntlu.myanimeinfo2.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.models.CharacterBodyResponse
import com.mrntlu.myanimeinfo2.utils.setGone
import com.mrntlu.myanimeinfo2.utils.setVisible
import kotlinx.android.synthetic.main.cell_character.view.*

class CharacterListAdapter (private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var isAdapterSet:Boolean=false
    private val LOADING_ITEM_HOLDER=0
    private val CHARACTER_HOLDER=1
    private var characterList:ArrayList<CharacterBodyResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER->{
                LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            }
            else->CharacterHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_character, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterHolder -> {
                holder.bind(characterList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int)=if (isAdapterSet) CHARACTER_HOLDER else LOADING_ITEM_HOLDER

    override fun getItemCount()=if (isAdapterSet) characterList.size else 1


    fun submitList(list: List<CharacterBodyResponse>) {
        characterList.apply {
            this.clear()
            this.addAll(list)
        }
        isAdapterSet=true
        notifyDataSetChanged()
    }

    class CharacterHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CharacterBodyResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.characterNameText.text=item.name
            itemView.characterRoleText.text=item.role

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
        fun onItemSelected(position: Int, item: CharacterBodyResponse)
    }
}