package com.example.tiktokdownloader.utils

import android.widget.ImageView
import com.bumptech.glide.Glide

class Util {
    companion object{
        fun loadImage(view: ImageView, srcImg: Int){
            Glide.with(view.context).load(srcImg).into(view)
        }
    }
}