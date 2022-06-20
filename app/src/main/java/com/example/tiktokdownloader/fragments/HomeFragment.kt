package com.example.tiktokdownloader.fragments

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.room.Room
import com.example.tiktokdownloader.models.TikTokModel
import com.example.tiktokdownloader.models.VideoModel
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.api.APIService
import com.example.tiktokdownloader.database.VideoDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import retrofit2.*
import java.io.*
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
class HomeFragment : Fragment() {
    val TAG= "HomeFrag"

    public var fileID:Long = 0

    var br :BroadcastReceiver =MyBroadcastReceiver()
    lateinit var downloadManager:DownloadManager

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



        view.btn_paste.setOnClickListener{
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
            api.getVideo(url= url).enqueue(object : Callback<TikTokModel> {
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
                                .into(thumbnail)
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

        view.btn_download_video.setOnClickListener {




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

    fun downloadFinish() {
        Log.e(TAG, "download finish")
        val uri:Uri = downloadManager.getUriForDownloadedFile(fileID)
        Log.e(TAG, uri.toString())

    }


    class MyBroadcastReceiver: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(p1?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE){
                p1.extras?.let {
                    val downloadFileId = it.getLong(DownloadManager.EXTRA_DOWNLOAD_ID)
                    val downloadManager = p0?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val uri:Uri = downloadManager.getUriForDownloadedFile(downloadFileId)
                    Log.e("Broad","Finish")
                    Log.e("Broad",uri.toString())
                }
            }

        }


    }





}