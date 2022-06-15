package com.example.tiktokdownloader.api

import com.example.tiktokdownloader.Model.DownloadAddr
import com.example.tiktokdownloader.Model.TikTokModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    //https://api2.musical.ly/aweme/v1/aweme/detail/?aweme_id=idVideo

    @GET("aweme/v1/aweme/detail/")
    fun getVideo(@Query("aweme_id") url: String):Call<TikTokModel>

    companion object{
        val BASE_URL = "https://api2.musical.ly"

        fun create(): APIService{
            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build()
            return retrofit.create(APIService::class.java)
        }
    }



}