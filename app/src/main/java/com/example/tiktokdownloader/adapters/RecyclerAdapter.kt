package com.example.tiktokdownloader.adapters

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
import com.example.tiktokdownloader.viewmodels.MyFileViewModel
import kotlinx.coroutines.*
import java.lang.NullPointerException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class RecyclerAdapter(
    listVideoModel: List<VideoModel>,
    ct: Context,
    recyclerViewClickInterface: RecyclerViewClickInterface
) :
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

    fun setList(data: ArrayList<VideoModel>) {
        listVideo = data
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

        when (videoModel.status) {
            DownloadManager.STATUS_PENDING -> holder.tv_status.text = "Pending..."
            DownloadManager.STATUS_RUNNING -> {
                holder.tv_status.text = "Downloading..."
                val scope = CoroutineScope(Dispatchers.Main)

                scope.launch {
                    holder.progressBar.progress = videoModel.percent
                }
            }
            DownloadManager.STATUS_FAILED -> holder.tv_status.text = "Failed"
            DownloadManager.STATUS_PAUSED -> holder.tv_status.text = "Pause"
            DownloadManager.STATUS_SUCCESSFUL -> {
                holder.tv_status.visibility = View.GONE
                holder.tv_percent.visibility = View.GONE
                holder.tv_speed.visibility = View.GONE
                holder.progressBar.visibility = View.GONE
            }
        }


        Glide.with(context).load(videoModel.thumbnail_url).override(84, 110)
            .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(4)))
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
        lateinit var tv_status: TextView
        lateinit var tv_percent: TextView
        lateinit var tv_speed: TextView
        lateinit var progressBar: ProgressBar


        init {
            tv_time = itemView.findViewById(R.id.tv_time)
            tv_title = itemView.findViewById(R.id.tv_title)
            tv_author = itemView.findViewById(R.id.tv_author)
            tv_filename = itemView.findViewById(R.id.tv_filename)
            tv_option = itemView.findViewById(R.id.tv_option)
            imageViewThumb = itemView.findViewById(R.id.imageViewThumb)
            checkBox = itemView.findViewById(R.id.checkBox)
            tv_status = itemView.findViewById(R.id.tv_status)
            tv_percent = itemView.findViewById(R.id.tv_percent)
            tv_speed = itemView.findViewById(R.id.tv_speed)
            progressBar = itemView.findViewById(R.id.progressBar)
        }

        fun bind(videoModel: VideoModel) {

        }
    }


}