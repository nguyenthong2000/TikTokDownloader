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
import com.bumptech.glide.Glide
import com.example.tiktokdownloader.models.TikTokModel
import com.example.tiktokdownloader.models.VideoModel
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.api.APIService
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
            downloadFromURL(tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[2])
            val video = VideoModel(
                0,
                ed_input.text.toString(),
                "",
                tiktokModel.aweme_detail.video.bit_rate[0].play_addr.url_list[2],
                tiktokModel.aweme_detail.music.play_url.url_list[0],
                tiktokModel.aweme_detail.video.origin_cover.url_list[0],
                tiktokModel.aweme_detail.desc,
                tiktokModel.aweme_detail.author.unique_id,
                "",
                "No Watermark",
                0,
                "",
                0

            )
            sendData.receiverMessage(video)

        }


        return view

    }


    @OptIn(InternalCoroutinesApi::class)
    fun downloadFromURL(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString()
        )
        downloadManager = activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val id = downloadManager.enqueue(request)
        context?.let { myFileViewModel.progress(it, id) }
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


}