package com.mrntlu.myanimeinfo2.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "searches"
)
data class UserSearch(@PrimaryKey(autoGenerate = true)val id:Int?=null, val search:String)