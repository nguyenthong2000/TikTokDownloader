package com.example.tiktokdownloader.fragment

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.DOWNLOAD_SERVICE
import android.opengl.Visibility
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.example.tiktokdownloader.Model.DownloadAddr
import com.example.tiktokdownloader.Model.TikTokModel
import com.example.tiktokdownloader.Model.Video
import com.example.tiktokdownloader.Model.VideoModel
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.api.APIService
import com.example.tiktokdownloader.database.VideoDAO
import com.example.tiktokdownloader.database.VideoDatabase
import com.squareup.picasso.Picasso
import io.reactivex.annotations.SchedulerSupport.IO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.*
import java.io.*
import android.net.Uri as Uri
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    val TAG= "HomeFrag"

    lateinit var ed_input:EditText
    lateinit var btn_paste:Button
    lateinit var btn_download:Button
    lateinit var btn_download_video:Button
    lateinit var tv_message:TextView
    lateinit var tv_video_name:TextView
    lateinit var tv_author_name:TextView
    lateinit var thumbNail:ImageView
    lateinit var downloadManager:DownloadManager
    var fileID:Long = 0

    var br :BroadcastReceiver =MyBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.applicationContext?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)

        ed_input = view.findViewById(R.id.ed_input)
        btn_paste = view.findViewById(R.id.btn_paste)
        btn_download = view.findViewById(R.id.btn_download)
        btn_download_video = view.findViewById(R.id.btn_download_video)
        tv_message = view.findViewById(R.id.tv_message)
        tv_video_name = view.findViewById(R.id.tv_video_name)
        tv_author_name =view.findViewById(R.id.tv_author_name)
        thumbNail = view.findViewById(R.id.thumbnail)

        btn_paste.setOnClickListener{
                val clipboardManager: ClipboardManager? =
                    activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipboard = clipboardManager?.primaryClip
                val item = clipboard?.getItemAt(0)
                ed_input.setText(item?.text.toString())

        }



        btn_download.setOnClickListener {
            val api: APIService = APIService.create()
            //https://www.tiktok.com/@amthuchungbaba_/video/7103276878366051611?is_from_webapp=1&sender_device=pc
            val url: String = ed_input.text.toString().substringAfterLast("/").substringBefore("?")
            api.getVideo(url=url).enqueue(object : Callback<TikTokModel> {
                override fun onResponse(
                    call: Call<TikTokModel>,
                    response: Response<TikTokModel>
                ) {
                    Toast.makeText(context, "Thanh cong", Toast.LENGTH_SHORT).show()

                    var tiktokModel: TikTokModel? = response.body()

                    if (tiktokModel != null) {
                        //try {

                            val uri: Uri = Uri.parse("a/b/c")

                            downloadFromURL(tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[0])

                            Log.e(TAG, tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[0])
                            Log.e(TAG, tiktokModel.aweme_detail.video.origin_cover.url_list[0])
                            Log.e(TAG, tiktokModel.aweme_detail.author.unique_id)
                            Log.e(TAG, tiktokModel.aweme_detail.desc)
                            Log.e(TAG, uri.toString())


                            Picasso.get()
                                .load(tiktokModel.aweme_detail.video.origin_cover.url_list[0])
                                .into(thumbNail)
                            tv_video_name.text = tiktokModel.aweme_detail.desc
                            tv_author_name.text = tiktokModel.aweme_detail.author.unique_id


                            val db = Room.databaseBuilder(
                                activity!!.applicationContext,
                                VideoDatabase::class.java,
                                "video.db"
                            ).allowMainThreadQueries().build()


                            val videoDAO = db.videoDAO()
                            //videoDAO.insertVideo(VideoModel(0,uri,tiktokModel.aweme_detail.author.unique_id,tiktokModel.aweme_detail.desc))

                            val videos: List<VideoModel> = videoDAO.selectAll()

                            for (video in videos) {
                                Log.e(TAG,"Database: " + video.id.toString())
                                Log.e(TAG, uri.toString())
                                Log.e(TAG, video.unique_id.toString())
                                Log.e(TAG, video.desc.toString())
                            }
                            /*
                        } catch (e: Exception) {
                            Log.e(TAG, e.message.toString())
                        }

                             */

                    }

                    Toast.makeText(context, "thanh cong", Toast.LENGTH_LONG).show()

                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<TikTokModel>, t: Throwable) {
                    tv_message.text = "Link fail. Please renew link."
                    Log.e(TAG, t.message.toString())
                }
            })
        }

        btn_download_video.setOnClickListener {




        }






        return view
    }

    fun downloadFromURL(url:String) {
        fileID = 0
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString()
        )
        downloadManager =activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        fileID = downloadManager.enqueue(request)


        //val uri:Uri = downloadManager.getUriForDownloadedFile(fileID)

    }

    fun downloadFinish(){
        Log.e(TAG, "download finish")
        val uri:Uri = downloadManager.getUriForDownloadedFile(fileID)
        Log.e(TAG, uri.toString())
    }


    class MyBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            Log.e("Broad","Finish")
        }
    }



}