package com.mrntlu.myanimeinfo2.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mrntlu.myanimeinfo2.models.UserSearch

@Dao
interface SearchDao {

    @Query("SELECT * FROM searches ORDER BY id DESC")
    fun getAllSearches():LiveData<List<UserSearch>>

    @Insert
    suspend fun insertNewSearch(search:UserSearch)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSearch(search:UserSearch)
}