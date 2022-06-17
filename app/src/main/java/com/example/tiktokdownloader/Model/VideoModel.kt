package com.example.tiktokdownloader.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoModel(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val uri:String,
    val unique_id:String,
    val desc:String)
