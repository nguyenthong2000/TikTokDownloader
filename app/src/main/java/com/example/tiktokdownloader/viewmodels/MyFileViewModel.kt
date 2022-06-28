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
import com.example.tiktokdownloader.models.Video
import com.example.tiktokdownloader.models.VideoModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.util.logging.LogManager

class MyFileViewModel(application: Application) : AndroidViewModel(application){
    var mutableLiveData : MutableLiveData<List<VideoModel>> = MutableLiveData<List<VideoModel>>()
    var listVideo : MutableList<VideoModel> = ArrayList<VideoModel>()

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
        mutableLiveData.postValue(videos)
    }

    fun addVideo(videoModel: VideoModel){
        listVideo.add(videoModel)
        mutableLiveData.postValue(listVideo)
    }

    fun progress(context: Context,id:Long){
        val scope = CoroutineScope(context = Dispatchers.IO)
        scope.async {
            while (true){
                val progress = getProgress(context,id)
                if (progress == -1 || progress == 100){
                    //Log.e("progress", "End")

                    break
                }else{
                    //Log.e("Progress", progress.toString())
                }
            }

        }
    }

    @SuppressLint("Range")
    private suspend fun getProgress(context: Context, dId: Long): Int {
        val q = DownloadManager.Query()
        q.setFilterById(dId)
        val cursor = (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).query(q)
        if (cursor.count <= 0) {
            cursor.close()
            return 0
        }
        val moveToFirst = cursor.moveToFirst()
        val bytes_downloaded = cursor.getInt(
            cursor
                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
        )
        val bytes_total =
            cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
        val downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        Log.e("Status", downloadStatus.toString())
        cursor.close()
//        return if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
//            100
//        } else (bytes_downloaded.toLong() * 100 / bytes_total.toLong()).toInt()
        return when(downloadStatus){
            DownloadManager.STATUS_SUCCESSFUL -> 100
            DownloadManager.STATUS_FAILED -> -1
            else -> (bytes_downloaded.toLong() * 100 / bytes_total.toLong()).toInt()

        }
    }

}
