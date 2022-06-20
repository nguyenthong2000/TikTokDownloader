package com.example.tiktokdownloader.models

data class Status(
    val allow_comment: Boolean,
    val allow_share: Boolean,
    val aweme_id: String,
    val download_status: Int,
    val in_reviewing: Boolean,
    val is_delete: Boolean,
    val is_prohibited: Boolean,
    val private_status: Int,
    val review_result: ReviewResult,
    val reviewed: Int,
    val self_see: Boolean
)