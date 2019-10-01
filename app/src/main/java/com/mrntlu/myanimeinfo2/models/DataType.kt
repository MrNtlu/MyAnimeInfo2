package com.mrntlu.myanimeinfo2.models

enum class DataType(val code:Int) {
    ANIME(0),
    MANGA(1);

    companion object{
        private val values = values()
        fun getByCode(code: Int) = values.first { it.code == code }
    }
}