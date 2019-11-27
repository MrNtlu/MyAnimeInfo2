package com.mrntlu.myanimeinfo2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrntlu.myanimeinfo2.R
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.repository.ServiceRepository
import com.mrntlu.myanimeinfo2.utils.Constants.TIME_OUT
import com.mrntlu.myanimeinfo2.utils.printLog
import kotlinx.coroutines.*

class MangaViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository= ServiceRepository()
    private var mJob: Job?=null

    //Manga
    fun getMangaByID(mal_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<MangaResponse> {
        val liveData= MutableLiveData<MangaResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: MangaResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaByID(mal_id)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError(getApplication<Application>().getString(R.string.timeout_try_again))
                }else{
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getTopMangas(page:Int,subtype:String,errorHandler: CoroutinesErrorHandler): LiveData<TopMangaResponse> {
        val liveData= MutableLiveData<TopMangaResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: TopMangaResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getTopMangas(page,subtype)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError(getApplication<Application>().getString(R.string.timeout_try_again))
                }else{
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getMangaCharactersByID(mal_id: Int,errorHandler: CoroutinesErrorHandler): LiveData<CharactersResponse> {
        val liveData= MutableLiveData<CharactersResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: CharactersResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaCharactersByID(mal_id)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError(getApplication<Application>().getString(R.string.timeout_try_again))
                }else{
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getMangaByGenre(genreID:Int,page:Int,errorHandler: CoroutinesErrorHandler): LiveData<MangaGenreSeasonResponse> {
        val liveData= MutableLiveData<MangaGenreSeasonResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: MangaGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaByGenre(genreID,page)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError(getApplication<Application>().getString(R.string.timeout_try_again))
                }else{
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getMangaBySearch(q:String,type:String, status:String, rated:String, score:String,page:Int,errorHandler: CoroutinesErrorHandler): LiveData<MangaSearchResponse> {
        val liveData= MutableLiveData<MangaSearchResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: MangaSearchResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaBySearch(q,type,status,rated,score,page)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError(getApplication<Application>().getString(R.string.timeout_try_again))
                }else{
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.let {
            if (it.isActive){
                printLog(message = "Canceled")
                it.cancel()
            }
        }
    }
}