package com.example.tiktokdownloader.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tiktokdownloader.Model.VideoModel

@Dao
interface VideoDAO {
    @Insert
    fun insertVideo(video: VideoModel)

    @Delete
    fun deleteVideo(video: VideoModel)

    @Query("SELECT * FROM videomodel")
    fun selectAll():List<VideoModel>
}