package com.example.tiktokdownloader.adapters

import android.content.Context
import android.net.Uri
import android.opengl.Visibility
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.models.Video
import com.example.tiktokdownloader.models.VideoModel

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var listVideo: List<VideoModel> =  listOf(
        VideoModel(0, "content://downloads/all_downloads/84","@abc","desc; abc"),
        VideoModel(1, "content://downloads/all_downloads/82","@abc","desc; abcd")
    )
    private lateinit var context : Context


    public fun setData(listVideoModel : List<VideoModel>, context: Context){
        this.listVideo =  listOf(
            VideoModel(0, "content://downloads/all_downloads/84","@abc","desc; abc"),
            VideoModel(1, "content://downloads/all_downloads/82","@abc","desc; abcd")
        )
        this.context = context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item2,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        var videoModel : VideoModel = listVideo.get(position)

        if (position==0){

                holder.tv_time.text = "abc"

        }else{
            holder.tv_time.visibility = View.GONE
        }



        holder.tv_title.text = videoModel.desc.toString()
        holder.tv_author.text = videoModel.unique_id.toString()
        holder.tv_filename.text = videoModel.uri.toString()
        holder.tv_option.text = videoModel.id.toString()
        //holder.videoView2.setVideoURI(Uri.parse(videoModel.uri))
        holder.videoView2.setVideoURI(Uri.parse("content://downloads/all_downloads/84.mp4"))


    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var tv_time:TextView
        var tv_title:TextView
        lateinit var tv_author:TextView
        lateinit var tv_filename:TextView
        lateinit var tv_option:TextView
        lateinit var videoView2:VideoView

        init {
            tv_time = itemView.findViewById(R.id.tv_time)
            tv_title = itemView.findViewById(R.id.tv_title)
            tv_author = itemView.findViewById(R.id.tv_author)
            tv_filename = itemView.findViewById(R.id.tv_filename)
            tv_option = itemView.findViewById(R.id.tv_option)
            videoView2 = itemView.findViewById(R.id.videoView2)
        }

    }
}