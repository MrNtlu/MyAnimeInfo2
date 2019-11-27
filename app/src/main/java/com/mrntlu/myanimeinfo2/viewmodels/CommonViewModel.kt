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

class CommonViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository= ServiceRepository()
    private var mJob: Job?=null

    fun getRecommendationsByID(type:String,mal_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<RecommendationsResponse> {
        val liveData= MutableLiveData<RecommendationsResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: RecommendationsResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getRecommendationsByID(type,mal_id)
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

    fun getCharacterInfoByID(character_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<CharacterInfoResponse> {
        val liveData= MutableLiveData<CharacterInfoResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: CharacterInfoResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getCharacterInfoByID(character_id)
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

    fun getReviewsByID(type:String,mal_id:Int,page:Int,errorHandler: CoroutinesErrorHandler): LiveData<ReviewsResponse> {
        val liveData= MutableLiveData<ReviewsResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: ReviewsResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getReviewsByID(type,mal_id,page)
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

    fun getPicturesByID(type:String,mal_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<PicturesResponse> {
        val liveData= MutableLiveData<PicturesResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response: PicturesResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getPicturesByID(type,mal_id)
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

    fun getUserProfile(username:String,errorHandler: CoroutinesErrorHandler): LiveData<UserProfileResponse> {
        val liveData=MutableLiveData<UserProfileResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            if (throwable.message!=null && throwable.message!!.contains("HTTP 404")) errorHandler.onError(throwable.message!!)
            else errorHandler.onError("Error! User not found or connection error.")
            throwable.printStackTrace()
        }){
            var response:UserProfileResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getUserProfile(username)
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

    fun getUserAnimeList(username:String,errorHandler: CoroutinesErrorHandler): LiveData<UserAnimeListResponse> {
        val liveData=MutableLiveData<UserAnimeListResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response:UserAnimeListResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getUserAnimeList(username)
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

    fun getUserMangaList(username:String,errorHandler: CoroutinesErrorHandler): LiveData<UserMangaListResponse> {
        val liveData=MutableLiveData<UserMangaListResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, _ ->
            errorHandler.onError(getApplication<Application>().getString(R.string.internet_error))
        }){
            var response:UserMangaListResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getUserMangaList(username)
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