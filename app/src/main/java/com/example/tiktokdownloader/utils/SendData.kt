package com.example.tiktokdownloader.utils

import com.example.tiktokdownloader.models.VideoModel


interface SendData {
    fun receiverMessage(videoModel: VideoModel)
}