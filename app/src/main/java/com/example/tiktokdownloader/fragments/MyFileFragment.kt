package com.example.tiktokdownloader.fragments

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktokdownloader.R
import com.example.tiktokdownloader.adapters.RecyclerAdapter
import com.example.tiktokdownloader.models.VideoModel
import kotlinx.android.synthetic.main.fragment_my_file.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyFileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyFileFragment : Fragment() {

    lateinit var recyclerAdapter: RecyclerAdapter
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerView : RecyclerView
    var linearLayoutManager:LinearLayoutManager = LinearLayoutManager(activity?.applicationContext)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_my_file, container, false)



        recyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.apply {

            try {
                layoutManager = LinearLayoutManager(activity)
                recyclerAdapter = RecyclerAdapter()
                recyclerView.adapter = recyclerAdapter
            }catch (e: Exception){
                Log.e("MyFile", e.message.toString())
            }


        }

        Toast.makeText(context, Uri.parse("content://downloads/all_downloads/84").toString(),Toast.LENGTH_LONG).show()






        return view
    }


}