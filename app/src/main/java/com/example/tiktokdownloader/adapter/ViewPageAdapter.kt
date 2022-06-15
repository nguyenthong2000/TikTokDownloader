package com.example.tiktokdownloader.adapter

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tiktokdownloader.fragment.ExploreFragment
import com.example.tiktokdownloader.fragment.HomeFragment
import com.example.tiktokdownloader.fragment.MyFileFragment
import com.example.tiktokdownloader.fragment.TrendingFragment


class ViewPageAdapter(fm: FragmentActivity): FragmentStateAdapter(fm){


    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> HomeFragment()
            1 -> MyFileFragment()
            2 -> TrendingFragment()
            3 -> ExploreFragment()
            else -> HomeFragment()
        }
    }



}