package com.example.tiktokdownloader.ui.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.adapters.RecyclerAdapter
import com.example.tiktokdownloader.database.VideoDatabase
import com.example.tiktokdownloader.models.VideoModel
import com.example.tiktokdownloader.utils.RecyclerViewClickInterface
import com.example.tiktokdownloader.utils.Util
import com.example.tiktokdownloader.viewmodels.MyFileViewModel
import kotlinx.android.synthetic.main.bottom_options_bar.*
import kotlinx.android.synthetic.main.fragment_my_file.*
import kotlinx.android.synthetic.main.recyclerview_item2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFileFragment : Fragment(), RecyclerViewClickInterface {

    private val myFileViewModel :MyFileViewModel by activityViewModels()

    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerView : RecyclerView
    var linearLayoutManager:LinearLayoutManager = LinearLayoutManager(activity?.applicationContext)
    lateinit var videos: List<VideoModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_my_file, container, false)

        val db= activity?.let {
            Room.databaseBuilder(
                it.applicationContext,
                VideoDatabase::class.java,
                "video.db"
            ).allowMainThreadQueries().build()
        }
        val videoDAO = db?.videoDAO()

        videos = videoDAO!!.selectAll()
        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.apply {

            try {
                layoutManager = LinearLayoutManager(activity)

                 activity?.let {
                     recyclerAdapter = RecyclerAdapter(videos,it.applicationContext, this@MyFileFragment)
                }
                recyclerView.adapter = recyclerAdapter
            }catch (e: Exception){
                Log.e("MyFile", e.message.toString())
            }
        }

//        myFileViewModel.getListMutableLiveData().observe(viewLifecycleOwner
//        ) {
//            activity?.let {
//                videos = videoDAO!!.selectAll()
//                recyclerAdapter = RecyclerAdapter(videos,it.applicationContext, this@MyFileFragment)
//            }
//            recyclerView.adapter = recyclerAdapter
//        }

        myFileViewModel.getListMutableLiveData().observe(viewLifecycleOwner, Observer {
            recyclerAdapter.setList(ArrayList(it))
            recyclerAdapter.notifyDataSetChanged()
        })



        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        recyclerAdapter.notifyDataSetChanged()
    }

    override fun onItemClick(position: Int) {

    }



    override fun onLongItemClick(position: Int) {
        Toast.makeText(context,"Long click",Toast.LENGTH_LONG).show()
        Util.loadImage(imgRePost,R.drawable.ic_repost)
        Util.loadImage(imgShare,R.drawable.ic_share)
        Util.loadImage(imgDelete,R.drawable.ic_delete)
        Util.loadImage(imgCancel,R.drawable.ic_cancel)
        bottom_options.visibility = View.VISIBLE

        imgCancel.setOnClickListener{
            bottom_options.visibility = View.GONE

        }
    }






}