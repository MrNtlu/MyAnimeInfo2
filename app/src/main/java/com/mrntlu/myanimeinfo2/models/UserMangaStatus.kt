package com.mrntlu.myanimeinfo2.models

enum class UserMangaStatus(val code:Int) {
    READING(1),
    COMPLETED(2),
    ONHOLD(3),
    DROPPED(4),
    PLANTOREAD(6);

    companion object{
        private val values = values()
        fun getByCode(code: Int) = values.first { it.code == code }
    }
}