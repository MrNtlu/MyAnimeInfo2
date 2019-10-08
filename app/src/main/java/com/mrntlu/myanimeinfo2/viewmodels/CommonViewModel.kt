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
import kotlinx.coroutines.*

class CommonViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository= ServiceRepository(application)
    private var mJob: Job?=null

    fun getRecommendationsByID(type:String,mal_id:Int,errorHandler: CoroutinesErrorHandler): LiveData<RecommendationsResponse> {
        val liveData= MutableLiveData<RecommendationsResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            errorHandler.onError(if (e.message==null) "Unknown Error!" else e.message!!)
            //todo test with manuel time & date
        }){
            var response: RecommendationsResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getRecommendationsByID(type,mal_id)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    errorHandler.onError("Error, timeout!")
                }else{
                    response?.let {
                        liveData.value=it
                    }
                }
            }
        }
        return liveData
    }

    fun getCharacterInfoByID(character_id:Int): LiveData<CharacterInfoResponse> {
        val liveData= MutableLiveData<CharacterInfoResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: CharacterInfoResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getCharacterInfoByID(character_id)
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

    fun getReviewsByID(type:String,mal_id:Int,page:Int): LiveData<ReviewsResponse> {
        val liveData= MutableLiveData<ReviewsResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: ReviewsResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getReviewsByID(type,mal_id,page)
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

    fun getPicturesByID(type:String,mal_id:Int): LiveData<PicturesResponse> {
        val liveData= MutableLiveData<PicturesResponse>()

        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: PicturesResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getPicturesByID(type,mal_id)
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
}