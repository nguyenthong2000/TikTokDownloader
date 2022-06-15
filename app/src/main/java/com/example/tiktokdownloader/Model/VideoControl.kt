package com.example.tiktokdownloader.Model

data class VideoControl(
    val allow_download: Boolean,
    val allow_duet: Boolean,
    val allow_dynamic_wallpaper: Boolean,
    val allow_music: Boolean,
    val allow_react: Boolean,
    val allow_stitch: Boolean,
    val draft_progress_bar: Int,
    val prevent_download_type: Int,
    val share_type: Int,
    val show_progress_bar: Int,
    val timer_status: Int
)