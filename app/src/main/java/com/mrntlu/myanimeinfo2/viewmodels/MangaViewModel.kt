package com.mrntlu.myanimeinfo2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.repository.ServiceRepository
import com.mrntlu.myanimeinfo2.utils.Constants.TIME_OUT
import kotlinx.coroutines.*

class MangaViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository= ServiceRepository(application)
    private var mJob: Job?=null

    //Manga
    fun getMangaByID(mal_id:Int){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: MangaResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaByID(mal_id)
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

    fun getTopMangas(page:Int,subtype:String){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: TopMangaResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getTopMangas(page,subtype)
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

    fun getMangaCharactersByID(mal_id: Int){
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

                    }
                }
            }
        }
    }

    fun getMangaByGenre(genreID:Int,page:Int){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: MangaGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getMangaByGenre(genreID,page)
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
}