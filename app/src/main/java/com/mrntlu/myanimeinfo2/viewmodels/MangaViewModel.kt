package com.mrntlu.myanimeinfo2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mrntlu.myanimeinfo2.interfaces.CoroutinesErrorHandler
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.repository.ServiceRepository
import com.mrntlu.myanimeinfo2.utils.Constants.TIME_OUT
import com.mrntlu.myanimeinfo2.utils.printLog
import kotlinx.coroutines.*

class MangaViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository= ServiceRepository(application)
    private var mJob: Job?=null

    //Manga
    fun getMangaByID(mal_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<MangaResponse> {
        val liveData= MutableLiveData<MangaResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            errorHandler.onError(if (e.message == null) "Unknown Error!" else e.message!!)
        }){
            var response: MangaResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaByID(mal_id)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError("Error, timeout!")
                }else{
                    //todo where you get the data
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getMangaReviewsByID(mal_id: Int,page: Int){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: ReviewsResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaReviewsByID(mal_id,page)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    //TODO error handling
                }else{
                    //todo where you get the data
                    response?.let {

                    }
                }
            }
        }
    }

    fun getTopMangas(page:Int,subtype:String,errorHandler: CoroutinesErrorHandler): LiveData<TopMangaResponse> {
        val liveData= MutableLiveData<TopMangaResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            errorHandler.onError(if (e.message == null) "Unknown Error!" else e.message!!)
        }){
            var response: TopMangaResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getTopMangas(page,subtype)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError("Error, timeout!")
                }else{
                    //todo where you get the data
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getMangaCharactersByID(mal_id: Int): LiveData<CharactersResponse> {
        val liveData= MutableLiveData<CharactersResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: CharactersResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaCharactersByID(mal_id)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    //TODO error handling
                }else{
                    //todo where you get the data
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

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            errorHandler.onError(if (e.message == null) "Unknown Error!" else e.message!!)
        }){
            var response: MangaGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaByGenre(genreID,page)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError("Error, timeout!")
                }else{
                    //todo where you get the data
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getMangaBySearch(q:String, page:Int){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: MangaSearchResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaBySearch(q,page)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    //TODO error handling
                }else{
                    //todo where you get the data
                    response?.let {

                    }
                }
            }
        }
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