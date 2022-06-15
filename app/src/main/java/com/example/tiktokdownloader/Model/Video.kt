package com.example.tiktokdownloader.Model

data class Video(
    val ai_dynamic_cover: AiDynamicCover,
    val ai_dynamic_cover_bak: AiDynamicCoverBak,
    val big_thumbs: Any,
    val bit_rate: List<BitRate>,
    val cdn_url_expired: Int,
    val cover: Cover,
    val download_addr: DownloadAddr,
    val download_suffix_logo_addr: DownloadSuffixLogoAddr,
    val duration: Int,
    val dynamic_cover: DynamicCover,
    val has_download_suffix_logo_addr: Boolean,
    val has_watermark: Boolean,
    val height: Int,
    val is_bytevc1: Int,
    val is_callback: Boolean,
    val is_h265: Int,
    val meta: String,
    val misc_download_addrs: String,
    val need_set_token: Boolean,
    val origin_cover: OriginCover,
    val play_addr: PlayAddrX,
    val ratio: String,
    val tags: Any,
    val width: Int
)