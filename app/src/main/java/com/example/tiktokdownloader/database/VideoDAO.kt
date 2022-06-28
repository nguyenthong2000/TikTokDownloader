package com.example.tiktokdownloader.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tiktokdownloader.models.VideoModel

@Dao
interface VideoDAO {
    @Insert
    fun insertVideo(video: VideoModel)

    @Delete
    fun deleteVideo(video: VideoModel)

    @Query("SELECT * FROM videomodel ORDER BY date DESC")
    fun selectAll(): List<VideoModel>

    @Query("SELECT * FROM videomodel ORDER BY date DESC limit 1")
    fun selectTop1(): List<VideoModel>


}