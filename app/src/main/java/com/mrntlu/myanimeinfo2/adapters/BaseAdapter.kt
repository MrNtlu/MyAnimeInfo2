package com.mrntlu.myanimeinfo2.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>:RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //Conditions
    protected var isAdapterSet=false
    protected var isErrorOccured=false
    protected var isPaginationLoading=false
    //Holders
    protected val LOADING_ITEM_HOLDER=0
    val ITEM_HOLDER=1
    protected val ERROR_HOLDER=2
    protected val NO_ITEM_HOLDER=3
    protected val PAGINATION_LOADING_HOLDER=4

    protected var errorMessage="Error!"
    protected var arrayList:ArrayList<T> = arrayListOf()

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
}