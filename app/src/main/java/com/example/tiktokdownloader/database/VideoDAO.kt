package com.example.tiktokdownloader.database

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.tiktokdownloader.models.Status
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

    @Query("UPDATE VideoModel SET status=:status where id=:id")
    fun updateStatusVideo(id : Long,status: Int)

    @Query("UPDATE VideoModel SET percent=:percent where id=:id")
    fun updatePercentVideo(id : Long,percent: Int)

    @Query("UPDATE VideoModel SET uri=:uri where id=:id")
    fun updateUriVideo(id : Long,uri: String)

    @Query("UPDATE VideoModel SET file_name=:fileName where id=:id")
    fun updateFileNametVideo(id : Long,fileName: String)

    @Query("UPDATE VideoModel SET date=:date where id=:id")
    fun updateDateVideo(id : Long,date: Long)


}