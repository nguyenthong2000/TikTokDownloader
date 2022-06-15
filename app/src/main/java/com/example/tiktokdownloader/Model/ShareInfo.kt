package com.example.tiktokdownloader.Model

data class ShareInfo(
    val share_desc: String,
    val share_desc_info: String,
    val share_qrcode_url: ShareQrcodeUrl,
    val share_title: String,
    val share_title_myself: String,
    val share_title_other: String,
    val share_url: String,
    val share_weibo_desc: String
)