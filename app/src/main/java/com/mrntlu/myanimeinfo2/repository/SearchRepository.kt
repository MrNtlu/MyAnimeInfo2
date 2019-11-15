package com.mrntlu.myanimeinfo2.repository

import android.app.Application
import com.mrntlu.myanimeinfo2.models.UserSearch
import com.mrntlu.myanimeinfo2.persistence.SearchDatabase

class SearchRepository(application: Application) {

    private val searchDao=SearchDatabase.getInstance(application).searchDao

    fun getSearches()=searchDao.getAllSearches()

    suspend fun insertNewSearch(search:UserSearch)=searchDao.insertNewSearch(search)

    suspend fun updateSearch(search: UserSearch)=searchDao.updateSearch(search)
}