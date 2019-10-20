package com.mrntlu.myanimeinfo2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.adapters.viewholders.ErrorItemViewHolder
import com.mrntlu.myanimeinfo2.adapters.viewholders.LoadingItemViewHolder
import com.mrntlu.myanimeinfo2.models.CharacterBodyResponse
import com.mrntlu.myanimeinfo2.utils.loadWithGlide
import kotlinx.android.synthetic.main.cell_character.view.*
import kotlinx.android.synthetic.main.cell_error.view.*

class CharacterListAdapter (private val interaction: Interaction? = null) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var isAdapterSet:Boolean=false
    private var isErrorOccured=false
    private val LOADING_ITEM_HOLDER=0
    private val CHARACTER_HOLDER=1
    private val ERROR_HOLDER=2
    private var errorMessage="Error!"
    private var characterList:ArrayList<CharacterBodyResponse> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            LOADING_ITEM_HOLDER-> LoadingItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_loading_item,parent,false))
            ERROR_HOLDER-> ErrorItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_error,parent,false))
            else->CharacterHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_character, parent, false), interaction)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CharacterHolder -> {
                holder.bind(characterList[position])
            }
            is ErrorItemViewHolder->{
                holder.itemView.errorText.text=errorMessage

                holder.itemView.errorRefreshButton.setOnClickListener {
                    interaction?.onErrorRefreshPressed()
                }
            }
        }
    }

    override fun getItemCount()=if (isAdapterSet) if (isErrorOccured) 1 else characterList.size else 1

    override fun getItemViewType(position: Int)=if (isAdapterSet){ if (isErrorOccured) ERROR_HOLDER else CHARACTER_HOLDER }else LOADING_ITEM_HOLDER

    fun submitList(list: List<CharacterBodyResponse>) {
        characterList.apply {
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

    class CharacterHolder constructor(itemView: View, private val interaction: Interaction?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: CharacterBodyResponse) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            itemView.characterNameText.text=item.name
            itemView.characterRoleText.text=item.role
            itemView.characterImage.loadWithGlide(item.image_url,itemView.characterProgressBar)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CharacterBodyResponse)

        fun onErrorRefreshPressed()
    }
}