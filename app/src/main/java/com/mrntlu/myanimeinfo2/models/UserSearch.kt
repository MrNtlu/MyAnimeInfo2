package com.mrntlu.myanimeinfo2.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "searches"
)
data class UserSearch(@PrimaryKey(autoGenerate = false)val id:Int, val search:String)