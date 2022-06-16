package com.example.tiktokdownloader.fragment

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Context.DOWNLOAD_SERVICE
import android.opengl.Visibility
import android.os.AsyncTask
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
import com.example.tiktokdownloader.Model.DownloadAddr
import com.example.tiktokdownloader.Model.TikTokModel
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.api.APIService
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
    lateinit var tv_message:TextView
    lateinit var tv_video_name:TextView
    lateinit var tv_author_name:TextView
    lateinit var videoView:VideoView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        tv_message = view.findViewById(R.id.tv_message)
        tv_video_name = view.findViewById(R.id.tv_video_name)
        tv_author_name =view.findViewById(R.id.tv_author_name)
        videoView = view.findViewById(R.id.videoView)

        btn_paste.setOnClickListener(object: View.OnClickListener{
            override fun onClick(p0: View?) {
                val clipboardManager: ClipboardManager? = activity?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipboard = clipboardManager?.primaryClip
                val item = clipboard?.getItemAt(0)
                ed_input.setText(item?.text.toString())
            }
        })

        btn_download.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val api: APIService = APIService.create()
                //https://www.tiktok.com/@amthuchungbaba_/video/7103276878366051611?is_from_webapp=1&sender_device=pc
                api.getVideo(url = "7103276878366051611").enqueue(object : Callback<TikTokModel>{
                    override fun onResponse(
                        call: Call<TikTokModel>,
                        response: Response<TikTokModel>
                    ) {
                        Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()

                        var videoModel: TikTokModel? = response.body()

                        if (videoModel != null) {
                            try {
                                Log.e(TAG, videoModel.aweme_detail.video.bit_rate[0].play_addr.url_list[0])
                                Log.e(TAG, videoModel.aweme_detail.author.unique_id)
                                Log.e(TAG, videoModel.aweme_detail.desc)
                                val url:String = "https://v16m-default.akamaized.net/6eaf26197d251d2269d7fcf634e5c666/62ab0159/video/tos/useast2a/tos-useast2a-pve-0037-aiso/7644f8a25df44ebca662e5f0ad69158a/?a=0&ch=0&cr=0&dr=0&lr=all&cd=0%7C0%7C0%7C0&cv=1&br=2670&bt=1335&btag=80000&cs=0&ds=6&ft=L4cJSoTzDJhNvyBiZqBaRfa3BMpBO5vBz-3nz7&mime_type=video_mp4&qs=0&rc=NWk8PDg2MzplNTtpNTozZEBpanVsaTU6Zmt4ZDMzZjgzM0A2Xl9gYDEvNTIxX14vNi0yYSMxNG0wcjQwZDZgLS1kL2Nzcw%3D%3D&l=202206160409110101920441020B62129F"
                                downloadFromURL(videoModel.aweme_detail.video.bit_rate[0].play_addr.url_list[0])
                                videoView.setVideoPath(url);

                                videoView.seekTo(1)


                                tv_video_name.text = videoModel.aweme_detail.desc
                                tv_author_name.text = videoModel.aweme_detail.author.unique_id
                                videoView.visibility = VideoView.VISIBLE

                            }catch(e: Exception){
                                Log.e(TAG,e.message.toString())
                            }

                        }

                        Toast.makeText(context,"thanh cong",Toast.LENGTH_LONG).show()

                    }

                    @SuppressLint("SetTextI18n")
                    override fun onFailure(call: Call<TikTokModel>, t: Throwable) {
                        tv_message.text = "Link fail. Please renew link."
                        Log.e(TAG,t.message.toString())
                    }
                })

            }
        })



        return view
    }

    fun downloadFromURL(url:String){
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("Downloading Your File")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            System.currentTimeMillis().toString()
        )
        val downloadManager =activity?.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

}