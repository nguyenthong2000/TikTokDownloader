package com.example.tiktokdownloader.fragment

import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tiktokdownloader.Model.DownloadAddr
import com.example.tiktokdownloader.Model.TikTokModel
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.api.APIService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
                api.getVideo(url = "1").enqueue(object : Callback<TikTokModel>{
                    override fun onResponse(
                        call: Call<TikTokModel>,
                        response: Response<TikTokModel>
                    ) {
                        Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()

                        var videoModel: TikTokModel? = response.body()

                        if (videoModel != null) {
                            try {
                                Log.e(TAG, videoModel.aweme_detail.video.bit_rate[0].play_addr.url_list[0])
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




}