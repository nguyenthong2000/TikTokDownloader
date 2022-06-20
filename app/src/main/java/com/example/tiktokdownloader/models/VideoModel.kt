package com.example.tiktokdownloader.models

import android.text.Editable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class VideoModel(
    @PrimaryKey(autoGenerate = true) val id:Int,
    var original_url: String,
    var uri:String,
    var video_url: String,
    var audio_url: String,
    var thumbnail_url: String,
    var desc:String,
    var unique_id:String,
    var file_name: String,
    val option: String,
    var date :Long)
