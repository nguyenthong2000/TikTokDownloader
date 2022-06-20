package com.example.tiktokdownloader.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.tiktokdownloader.ui.fragments.ExploreFragment
import com.example.tiktokdownloader.ui.fragments.HomeFragment
import com.example.tiktokdownloader.ui.fragments.MyFileFragment
import com.example.tiktokdownloader.ui.fragments.TrendingFragment


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