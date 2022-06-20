package com.example.tiktokdownloader.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.models.VideoModel
import com.example.tiktokdownloader.utils.RecyclerViewClickInterface
import com.example.tiktokdownloader.utils.Util
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RecyclerAdapter(listVideoModel: List<VideoModel>, ct: Context, recyclerViewClickInterface: RecyclerViewClickInterface) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    lateinit var listVideo: List<VideoModel>
    lateinit var context: Context
    lateinit var recyclerViewClick: RecyclerViewClickInterface

    @RequiresApi(Build.VERSION_CODES.O)
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        listVideo = listVideoModel
        context = ct
        recyclerViewClick = recyclerViewClickInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item2, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {

        var videoModel: VideoModel = listVideo.get(position)
        val time =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(videoModel.date), ZoneId.systemDefault())
                .toLocalDate()
        if (position == 0) {
            val now: LocalDate = LocalDateTime.now().toLocalDate()

            Log.e("Date", time.toString())
            if (now == time) {
                holder.tv_time.text = "Today"
            } else {
                holder.tv_time.text = time.format(formatter)
            }
        } else {
            if (time == LocalDateTime.ofInstant(
                    Instant.ofEpochMilli(listVideo.get(position - 1).date),
                    ZoneId.systemDefault()
                ).toLocalDate()
            ) {
                holder.tv_time.visibility = View.GONE
            } else {
                holder.tv_time.visibility = View.VISIBLE
                holder.tv_time.text = time.format(formatter)
            }


        }



        holder.tv_title.text = videoModel.desc
        holder.tv_author.text = "@" + videoModel.unique_id
        holder.tv_filename.text = "File: ${videoModel.file_name}"
        holder.tv_option.text = videoModel.option

        Glide.with(context).load(videoModel.thumbnail_url).override(84,110).
        apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(4)))
            .into(holder.imageViewThumb)

        holder.itemView.setOnLongClickListener {
            holder.checkBox.visibility = View.VISIBLE
            recyclerViewClick.onLongItemClick(position)
            return@setOnLongClickListener true
        }

    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return listVideo.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv_time: TextView
        lateinit var tv_title: TextView
        lateinit var tv_author: TextView
        lateinit var tv_filename: TextView
        lateinit var tv_option: TextView
        lateinit var imageViewThumb: ImageView
        lateinit var checkBox: CheckBox

        init {
            tv_time = itemView.findViewById(R.id.tv_time)
            tv_title = itemView.findViewById(R.id.tv_title)
            tv_author = itemView.findViewById(R.id.tv_author)
            tv_filename = itemView.findViewById(R.id.tv_filename)
            tv_option = itemView.findViewById(R.id.tv_option)
            imageViewThumb = itemView.findViewById(R.id.imageViewThumb)
            checkBox = itemView.findViewById(R.id.checkBox)
        }

        fun hideBottomBar(){
            checkBox.visibility = View.GONE
        }



    }

}