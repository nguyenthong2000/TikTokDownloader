package com.example.tiktokdownloader.Model

data class DownloadAddr(
    val data_size: Int,
    val height: Int,
    val uri: String,
    val url_list: List<String>,
    val width: Int
)