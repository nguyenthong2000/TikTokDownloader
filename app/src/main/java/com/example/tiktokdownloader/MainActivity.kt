package com.example.tiktokdownloader

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.example.tiktokdownloader.adapters.ViewPageAdapter
import com.example.tiktokdownloader.database.VideoDAO
import com.example.tiktokdownloader.database.VideoDatabase
import com.example.tiktokdownloader.models.VideoModel
import com.example.tiktokdownloader.utils.GetRealPath
import com.example.tiktokdownloader.utils.SendData
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SendData {
    private val TAG:String="MainActivity"
    lateinit var viewpage: ViewPager2
    lateinit var bottom_nav : BottomNavigationView
    lateinit var db: VideoDatabase
    lateinit var videoDAO: VideoDAO

    var listVideo: MutableList<VideoModel> =ArrayList<VideoModel>()

    private val broad = object : BroadcastReceiver(){
        @SuppressLint("SimpleDateFormat")
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.e("Broad","Start")
            val getRealPath = GetRealPath()
            if(p1?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE){

                p1.extras?.let {
                    try {
                        val downloadFileId = it.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
                        val downloadManager = p0?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        val uri: Uri = downloadManager.getUriForDownloadedFile(downloadFileId)
                        val mineType: String? =  downloadManager.getMimeTypeForDownloadedFile(downloadFileId)


                        val path: String? = getRealPath.getRealPath(p0,uri)

                        val file : File = File(path)

                        Log.e("Broad","Finish")
                        Log.e("Broad",uri.toString())

                        if (path != null && mineType !=null && file != null) {
                            listVideo[0].uri = path
                            listVideo[0].file_name = getFileName(path,mineType)
                            listVideo[0].date = file.lastModified()
                        }

                        videoDAO.insertVideo(listVideo[0])
                        listVideo.clear()



                        val videos: List<VideoModel> = videoDAO.selectAll()
                        for (video in videos){
                            Log.e("Main", video.uri)
                        }

                    }catch (e: Exception){
                        Log.e("Broad",e.message.toString())
                    }

                }
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()




        viewpage = findViewById(R.id.viewpage)
        bottom_nav = findViewById(R.id.bottom_nav)

        db= Room.databaseBuilder(
            applicationContext,
            VideoDatabase::class.java,
            "video.db"
        ).allowMainThreadQueries().build()
        videoDAO = db.videoDAO()


        val adapter = ViewPageAdapter(this)
        viewpage.adapter = adapter

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> {
                    viewpage.setCurrentItem(0, true)
                }
                R.id.menu_myfile -> {
                    viewpage.setCurrentItem(1, true)
                }
                R.id.menu_trending -> {
                    viewpage.setCurrentItem(2, true)
                }
                R.id.menu_explore -> {
                    viewpage.setCurrentItem(3, true)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        viewpage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> bottom_nav.selectedItemId = R.id.menu_home
                    1 -> bottom_nav.selectedItemId = R.id.menu_myfile
                    2 -> bottom_nav.selectedItemId = R.id.menu_trending
                    3 -> bottom_nav.selectedItemId = R.id.menu_explore

                }
            }
        })

    }



    override fun onStart() {
        super.onStart()
        registerReceiver(broad, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broad)
    }

    override fun receiverMessage(videoModel: VideoModel) {
        listVideo.add(videoModel)
    }

    fun getFileName(path: String, mineType : String ): String{
        return path.substringAfterLast("/") + "." + mineType.substringAfterLast("/")
    }



}