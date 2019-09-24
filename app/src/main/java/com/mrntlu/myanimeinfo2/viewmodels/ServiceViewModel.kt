package com.mrntlu.myanimeinfo2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.myanimeinfo2.models.AnimeResponse
import com.mrntlu.myanimeinfo2.repository.ServiceRepository
import com.mrntlu.myanimeinfo2.utils.printLog
import kotlinx.coroutines.*
import retrofit2.Response

class ServiceViewModel(application: Application): AndroidViewModel(application) {

    private val TIME_OUT=5000L
    private val serviceRepository=ServiceRepository(application)
    private var mJob:Job?=null

    fun getDataByID(mal_id:Int){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
        }){
            var response:AnimeResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeByID(mal_id)
            }
            withContext(Dispatchers.Main){
                if (job==null){
                    printLog(message = "Error")
                }else{
                    response?.let {
                        printLog(message = it.title)
                    }
                }
            }
        }
    }
}