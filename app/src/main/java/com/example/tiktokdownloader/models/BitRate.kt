package com.example.tiktokdownloader.models

data class BitRate(
    val bit_rate: Int,
    val dub_infos: Any,
    val gear_name: String,
    val is_bytevc1: Int,
    val is_h265: Int,
    val play_addr: PlayAddrX,
    val quality_type: Int
)