package com.example.tiktokdownloader.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TikTokModel(
    @SerializedName("aweme_detail")
    @Expose
    val aweme_detail: AwemeDetail,
    val extra: Extra,
    val log_pb: LogPb,
    val status_code: Int


)