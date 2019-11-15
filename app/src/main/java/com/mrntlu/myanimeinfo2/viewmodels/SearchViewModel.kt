package com.mrntlu.myanimeinfo2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.UserSearch
import com.mrntlu.myanimeinfo2.repository.SearchRepository
import com.mrntlu.myanimeinfo2.utils.Constants.TIME_OUT
import com.mrntlu.myanimeinfo2.utils.printLog
import kotlinx.coroutines.*

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private var searchRepository = SearchRepository(application)
    private var mJob: Job? = null

    fun getSearches()=searchRepository.getSearches()

    fun insertOrUpdateSearch(search:UserSearch,isInserting:Boolean,errorHandler: CoroutinesErrorHandler): LiveData<UserSearch> {
        val liveData= MutableLiveData<UserSearch>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            val job=withTimeoutOrNull(TIME_OUT) {
                if (isInserting) searchRepository.insertNewSearch(search)
                else searchRepository.updateSearch(search)
            }
            withContext(Dispatchers.Main){
                if (job == null) {
                    errorHandler.onError("Timed out!")
                }else{
                    liveData.value=search
                }
            }
        }
        return liveData
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.let {
            if (it.isActive){
                printLog(message = "Canceled Search")
                it.cancel()
            }
        }
    }
}