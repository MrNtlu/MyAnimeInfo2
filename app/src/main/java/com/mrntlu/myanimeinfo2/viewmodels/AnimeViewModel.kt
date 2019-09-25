package com.mrntlu.myanimeinfo2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.mrntlu.myanimeinfo2.models.*
import com.mrntlu.myanimeinfo2.repository.ServiceRepository
import com.mrntlu.myanimeinfo2.utils.Constants.TIME_OUT
import kotlinx.coroutines.*

class AnimeViewModel(application: Application): AndroidViewModel(application) {

    private val serviceRepository=ServiceRepository(application)
    private var mJob:Job?=null

    //Anime
    fun getAnimeByID(mal_id:Int){
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response:AnimeResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeByID(mal_id)
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

    fun getAnimeCharactersByID(mal_id: Int) {
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response:CharactersResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeCharactersByID(mal_id)
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

    fun getTopAnimes(page:Int,subtype:String) {
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: TopAnimeResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getTopAnimes(page,subtype)
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

    fun getAnimeSchedule() {
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: AnimeScheduleResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeSchedule()
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

    fun getAnimeByGenre(genreID:Int,page:Int) {
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: AnimeGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeByGenre(genreID,page)
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

    fun getAnimeBySeason(year:Int,season:String) {
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: AnimeGenreSeasonResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeBySeason(year,season)
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

    fun getAnimeBySearch(q:String,page:Int) {
        mJob=viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, e ->
            e.printStackTrace()
            //todo error handling
            //todo test with manuel time & date
        }){
            var response: AnimeSearchResponse?=null
            val job= withTimeoutOrNull(TIME_OUT){
                response=serviceRepository.getAnimeBySearch(q,page)
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