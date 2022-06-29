package com.example.tiktokdownloader.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import androidx.room.RoomMasterTable
import com.example.tiktokdownloader.database.VideoDatabase
import com.example.tiktokdownloader.models.Status
import com.example.tiktokdownloader.models.Video
import com.example.tiktokdownloader.models.VideoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.util.logging.LogManager

class MyFileViewModel(application: Application) : AndroidViewModel(application){
    var mutableLiveData : MutableLiveData<List<VideoModel>> = MutableLiveData<List<VideoModel>>()

    val db = Room.databaseBuilder(getApplication<Application>().applicationContext, VideoDatabase::class.java, "video.db")
        .allowMainThreadQueries().build()

    val videoDAO = db.videoDAO()

    init {
        getAllVideos()
    }


    fun getListMutableLiveData(): MutableLiveData<List<VideoModel>> {

        return mutableLiveData
    }

    fun getAllVideos(){
        val videos = videoDAO!!.selectAll()
        mutableLiveData.value = videos


    }

    fun addVideo(videoModel: VideoModel){
        videoDAO.insertVideo(videoModel)
        getAllVideos()
    }

    fun updateVideo(id: Long, status: Int, percent:Int){
        videoDAO.updateStatusVideo(id, status)
        videoDAO.updatePercentVideo(id,percent)
        getAllVideos()
    }





}
