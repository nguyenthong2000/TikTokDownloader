package com.example.tiktokdownloader.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktokdownloader.models.VideoModel

class MyFileViewModel: ViewModel() {
    lateinit var mutableLiveData : MutableLiveData<List<VideoModel>>
    lateinit var listVideo : MutableList<VideoModel>

    fun init(){
        mutableLiveData = MutableLiveData<List<VideoModel>>()


    }



    @JvmName("getMutableLiveData1")
    fun getMutableLiveData(): MutableLiveData<List<VideoModel>> {
        return mutableLiveData
    }

    fun addVideo(videoModel: VideoModel){
        listVideo.add(videoModel)
    }

}
