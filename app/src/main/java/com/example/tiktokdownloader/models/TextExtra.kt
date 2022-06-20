package com.example.tiktokdownloader.models

data class TextExtra(
    val end: Int,
    val hashtag_id: String,
    val hashtag_name: String,
    val is_commerce: Boolean,
    val sec_uid: String,
    val start: Int,
    val sub_type: Int,
    val type: Int,
    val user_id: String
)