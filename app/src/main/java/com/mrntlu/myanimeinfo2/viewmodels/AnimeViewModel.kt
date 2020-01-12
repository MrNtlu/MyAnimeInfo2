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

class AnimeViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository=ServiceRepository()
    private var mJob:Job?=null

    //Anime
    fun getAnimeByID(mal_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<AnimeResponse> {
        val liveData=MutableLiveData<AnimeResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response:AnimeResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeByID(mal_id)
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

    fun getAnimeCharactersByID(mal_id: Int,errorHandler: CoroutinesErrorHandler): LiveData<CharactersResponse> {
        val liveData=MutableLiveData<CharactersResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response:CharactersResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeCharactersByID(mal_id)
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

    fun getTopAnimes(page:Int,subtype:String,errorHandler: CoroutinesErrorHandler): LiveData<TopAnimeResponse> {
        val liveData=MutableLiveData<TopAnimeResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: TopAnimeResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getTopAnimes(page,subtype)
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

    fun getAnimeSchedule(errorHandler: CoroutinesErrorHandler): LiveData<AnimeScheduleResponse> {
        val liveData=MutableLiveData<AnimeScheduleResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: AnimeScheduleResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeSchedule()
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

    fun getAnimeByGenre(genreID:Int,page:Int,errorHandler: CoroutinesErrorHandler): LiveData<AnimeGenreSeasonResponse> {
        val liveData=MutableLiveData<AnimeGenreSeasonResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: AnimeGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeByGenre(genreID,page)
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

    fun getProducerInfoByID(mal_id: Int,errorHandler: CoroutinesErrorHandler):LiveData<ProducerInfoResponse>{
        val liveData= MutableLiveData<ProducerInfoResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: ProducerInfoResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getProducerInfoByID(mal_id)
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

    fun getAnimeBySeason(year:String,season:String,errorHandler: CoroutinesErrorHandler): LiveData<AnimeGenreSeasonResponse> {
        val liveData= MutableLiveData<AnimeGenreSeasonResponse>()

        printLog(message = "Year $year season $season")

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, a ->
            printLog(message = "${a.message}")
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: AnimeGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeBySeason(year,season)
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

    fun getAnimeBySearch(q:String,type:String, status:String, rated:String, score:String,page:Int,errorHandler: CoroutinesErrorHandler): LiveData<AnimeSearchResponse> {
        val liveData= MutableLiveData<AnimeSearchResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: AnimeSearchResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeBySearch(q,type,status,rated,score,page)
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