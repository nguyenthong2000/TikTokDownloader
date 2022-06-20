package com.example.tiktokdownloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tiktokdownloader.adapters.ViewPageAdapter
import com.example.tiktokdownloader.fragments.ExploreFragment
import com.example.tiktokdownloader.fragments.HomeFragment
import com.example.tiktokdownloader.fragments.MyFileFragment
import com.example.tiktokdownloader.fragments.TrendingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val TAG:String="MainActivity"
    lateinit var viewPager: ViewPager2
    lateinit var bottomNav : BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        supportActionBar?.hide()

        viewPager = findViewById(R.id.viewpage)
        bottomNav = findViewById(R.id.bottom_nav)

        val fragments : ArrayList<Fragment> = arrayListOf(
            HomeFragment(),
            MyFileFragment(),
            TrendingFragment(),
            ExploreFragment()
        )

        val adapter = ViewPageAdapter(this)
        viewPager.adapter = adapter

        bottomNav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_home -> {
                    viewPager.setCurrentItem(0, true)
                }
                R.id.menu_myfile -> {
                    viewPager.setCurrentItem(1, true)
                }
                R.id.menu_trending -> {
                    viewPager.setCurrentItem(2, true)
                }
                R.id.menu_explore -> {
                    viewPager.setCurrentItem(3, true)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> bottomNav.selectedItemId = R.id.menu_home
                    1 -> bottomNav.selectedItemId = R.id.menu_myfile
                    2 -> bottomNav.selectedItemId = R.id.menu_trending
                    3 -> bottomNav.selectedItemId = R.id.menu_explore

                }
            }
        })
    }
}