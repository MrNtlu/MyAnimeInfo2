package com.mrntlu.myanimeinfo2.models

enum class DialogType(val code:Int) {
    GENRE(0),
    PRODUCER(1);

    companion object{
        private val values = values()
        fun getByCode(code: Int) = values.first { it.code == code }
    }
}