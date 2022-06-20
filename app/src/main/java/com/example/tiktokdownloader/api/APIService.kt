package com.example.tiktokdownloader.api

import com.example.tiktokdownloader.models.TikTokModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface APIService {
    //https://api2.musical.ly/aweme/v1/aweme/detail/?aweme_id=idVideo

    @GET("aweme/v1/aweme/detail/")
    fun getVideo(@Query("aweme_id") url: String):Call<TikTokModel>

    @Streaming
    @GET
    suspend  fun downloadFile(@Url fileUrl: String):Response<ResponseBody>

    companion object{
        val BASE_URL = "https://api2.musical.ly"

        fun create(): APIService{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL).build()
            return retrofit.create(APIService::class.java)
        }
    }



}