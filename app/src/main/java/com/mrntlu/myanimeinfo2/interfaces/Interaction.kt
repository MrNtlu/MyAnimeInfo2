package com.mrntlu.myanimeinfo2.interfaces

interface Interaction<T> {
    fun onItemSelected(position: Int, item: T)

    fun onErrorRefreshPressed()
}