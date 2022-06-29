package com.example.tiktokdownloader.ui.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.example.tiktokdownloader.models.TikTokModel
import com.example.tiktokdownloader.models.VideoModel
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.api.APIService
import com.example.tiktokdownloader.database.VideoDAO
import com.example.tiktokdownloader.database.VideoDatabase
import com.example.tiktokdownloader.utils.SendData
import com.example.tiktokdownloader.viewmodels.MyFileViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.*
import retrofit2.*
import java.io.*
import java.util.*
import android.net.Uri as Uri

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment() : Fragment() {
    val TAG = "HomeFrag"
    private val myFileViewModel:MyFileViewModel by activityViewModels()

    lateinit var downloadManager: DownloadManager
    lateinit var sendData: SendData
    lateinit var tiktokModel: TikTokModel
    lateinit var db: VideoDatabase
    lateinit var videoDAO: VideoDAO


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)


        sendData = activity as SendData

        db= context?.let {
            Room.databaseBuilder(
                it,
                VideoDatabase::class.java,
                "video.db"
            ).allowMainThreadQueries().build()
        }!!

        videoDAO = db.videoDAO()


        view.btn_paste.setOnClickListener {
            val clipboardManager: ClipboardManager? =
                activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipboard = clipboardManager?.primaryClip
            val item = clipboard?.getItemAt(0)
            ed_input.setText(item?.text.toString())

        }



        view.btn_download.setOnClickListener {
            val api: APIService = APIService.create()
            //https://www.tiktok.com/@amthuchungbaba_/video/7103276878366051611?is_from_webapp=1&sender_device=pc
            val url: String = ed_input.text.toString().substringAfterLast("/").substringBefore("?")
            api.getVideo(url = url).enqueue(object : Callback<TikTokModel> {
                override fun onResponse(
                    call: Call<TikTokModel>,
                    response: Response<TikTokModel>
                ) {

                    tiktokModel = response.body()!!

                    try {

                        Log.e(TAG, tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[0])
                        Log.e(TAG, tiktokModel.aweme_detail.video.origin_cover.url_list[0])
                        Log.e(TAG, tiktokModel.aweme_detail.author.unique_id)
                        Log.e(TAG, tiktokModel.aweme_detail.desc)

                        Glide.with(activity!!.applicationContext).load(tiktokModel.aweme_detail.video.origin_cover.url_list[0]).override(60,80).into(thumbnail)
                        tv_video_name.text = tiktokModel.aweme_detail.desc
                        tv_author_name.text = tiktokModel.aweme_detail.author.unique_id

                        btn_download_audio.visibility = View.VISIBLE
                        btn_download_video.visibility = View.VISIBLE
                        btn_download_thumbnail.visibility = View.VISIBLE
                        thumbnail.visibility = View.VISIBLE
                        tv_video_name.visibility = View.VISIBLE
                        tv_author_name.visibility = View.VISIBLE

                    } catch (e: Exception) {
                        Log.e(TAG, e.message.toString())
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<TikTokModel>, t: Throwable) {
                    tv_message.text = "Link fail. Please renew link."
                    Log.e(TAG, t.message.toString())
                }
            })
        }

        view.btn_download_video.setOnClickListener {
            val id:Long = downloadFromURL(tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[2])
            val video = VideoModel(
                id,
                ed_input.text.toString(),
                "",
                tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[2],
                tiktokModel.aweme_detail.music.play_url.url_list[0],
                tiktokModel.aweme_detail.video.origin_cover.url_list[0],
                tiktokModel.aweme_detail.desc,
                tiktokModel.aweme_detail.author.unique_id,
                "",
                "No Watermark",
                System.currentTimeMillis(),
                DownloadManager.STATUS_PENDING,
                0

            )
            myFileViewModel.addVideo(video)

            sendData.receiverMessage(video)

        }


        return view

    }


    @OptIn(InternalCoroutinesApi::class)
    fun downloadFromURL(url: String) : Long {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString()
        )
        downloadManager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val id = downloadManager.enqueue(request)


        val scope = CoroutineScope(Dispatchers.Main)
        scope.launch {
            while (true){
                val progress = context?.let { getProgress(it,id) }!!
                val status = context?.let { getStatus(it,id) }!!
                Log.e("Progress", progress.toString())
                if (progress == -1 || progress == 100){
                    Log.e("progress", "End")

                    break
                }else{
                    myFileViewModel.updateVideo(id,status,progress)
                }
                delay(10)
            }

        }



//        requireActivity().applicationContext.let {
//            myFileViewModel.progress(it,id)
//        }
//
//        context?.let { myFileViewModel.progress(it, id) }
        return id
    }


    override fun onResume() {
        super.onResume()
//        context?.let {
//            LocalBroadcastManager.getInstance(it).registerReceiver(br,
//                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//            )
//        }

    }

    override fun onPause() {
        super.onPause()

//        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(br) }
    }

    override fun onDestroy() {
        super.onDestroy()
//        context?.let { LocalBroadcastManager.getInstance(it).unregisterReceiver(br) }
    }

    fun progress(context: Context,id:Long){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.async {
            while (true){
                val progress = getProgress(context,id)
                val status = getStatus(context,id)
                Log.e("Progress", progress.toString())
                if (progress == -1 || progress == 100){
                    Log.e("progress", "End")

                    break
                }else{
                    myFileViewModel.updateVideo(id,status,progress)
                }
                delay(100)
            }

        }
    }

    @SuppressLint("Range")
    private suspend fun getProgress(context: Context, dId: Long): Int{
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
        cursor.close()
//        return if (downloadStatus == DownloadManager.STATUS_SUCCESSFUL) {
//            100
//        } else (bytes_downloaded.toLong() * 100 / bytes_total.toLong()).toInt()

        val progess: Int = when(downloadStatus) {
            DownloadManager.STATUS_SUCCESSFUL -> 100
            DownloadManager.STATUS_FAILED -> -1
            else -> (bytes_downloaded.toLong() * 100 / bytes_total.toLong()).toInt()
        }
        return progess
    }

    @SuppressLint("Range")
    private suspend fun getStatus(context: Context, dId: Long): Int{
        val q = DownloadManager.Query()
        q.setFilterById(dId)
        val cursor = (context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager).query(q)
        if (cursor.count <= 0) {
            cursor.close()
            return 0
        }
        val moveToFirst = cursor.moveToFirst()
        val downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
        Log.e("Status", downloadStatus.toString())
        cursor.close()


        return downloadStatus
    }


}