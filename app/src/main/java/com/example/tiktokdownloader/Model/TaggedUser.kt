package com.example.tiktokdownloader.Model

data class TaggedUser(
    val avatar_168x168: Avatar168x168,
    val follow_status: Int,
    val follower_status: Int,
    val interest_level: Int,
    val nickname: String,
    val uid: String,
    val unique_id: String
)