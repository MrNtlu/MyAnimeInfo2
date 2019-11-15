package com.mrntlu.myanimeinfo2.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mrntlu.myanimeinfo2.models.UserSearch

@Database(entities = [UserSearch::class],version = 1)
abstract class SearchDatabase:RoomDatabase() {

    companion object{
        const val DATABASE_NAME="search_db"

        @Volatile
        private var INSTANCE: SearchDatabase? = null

        fun getInstance(context: Context): SearchDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SearchDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    abstract val searchDao:SearchDao
}