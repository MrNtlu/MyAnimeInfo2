package com.mrntlu.myanimeinfo2.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mrntlu.myanimeinfo2.interfaces.Interaction
import kotlinx.android.synthetic.main.cell_error.view.*

abstract class BaseAdapter<T>(open val interaction: Interaction<T>? = null):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //Conditions
    private var isAdapterSet=false
    private var isErrorOccured=false
    private var isPaginationLoading=false
    //Holders
    protected val LOADING_ITEM_HOLDER=0
    val ITEM_HOLDER=1
    protected val ERROR_HOLDER=2
    protected val NO_ITEM_HOLDER=3
    protected val PAGINATION_LOADING_HOLDER=4

    protected open var errorMessage="Error!"
    private var arrayList:ArrayList<T> = arrayListOf()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_HOLDER -> {
                (holder as ItemHolder<T>).bind(arrayList[position])
            }
            ERROR_HOLDER ->{
                holder.itemView.errorText.text=errorMessage

                holder.itemView.errorRefreshButton.setOnClickListener {
                    interaction?.onErrorRefreshPressed()
                }
            }
        }
    }

    override fun getItemViewType(position: Int)=if (isAdapterSet){
        when{
            isErrorOccured->ERROR_HOLDER
            arrayList.size==0->NO_ITEM_HOLDER
            isPaginationLoading && position==arrayList.size->PAGINATION_LOADING_HOLDER
            else->ITEM_HOLDER
        }
    }else LOADING_ITEM_HOLDER

    override fun getItemCount()=if (isAdapterSet && !isErrorOccured && arrayList.size!=0 && !isPaginationLoading) arrayList.size
    else if (isAdapterSet && !isErrorOccured && isPaginationLoading) arrayList.size+1
    else 1

    fun submitList(list: List<T>) {
        arrayList.apply {
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

    //Pagination
    fun submitPaginationLoading(){
        isPaginationLoading=true
        notifyDataSetChanged()
    }

    fun submitPaginationError(){
        isPaginationLoading=false
        notifyDataSetChanged()
    }

    fun submitPaginationList(list:List<T>){
        isPaginationLoading=false
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    abstract class ItemHolder<T> constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
    }
}