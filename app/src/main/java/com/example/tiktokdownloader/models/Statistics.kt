package com.example.tiktokdownloader.models

data class Statistics(
    val aweme_id: String,
    val collect_count: Int,
    val comment_count: Int,
    val digg_count: Int,
    val download_count: Int,
    val forward_count: Int,
    val lose_comment_count: Int,
    val lose_count: Int,
    val play_count: Int,
    val share_count: Int,
    val whatsapp_share_count: Int
)