package com.mrntlu.myanimeinfo2.models

enum class UserAnimeStatus(val code:Int) {
    WATCHING(1),
    COMPLETED(2),
    ONHOLD(3),
    DROPPED(4),
    PLANTOWATCH(6);

    companion object{
        private val values = values()
        fun getByCode(code: Int) = values.first { it.code == code }
    }
}