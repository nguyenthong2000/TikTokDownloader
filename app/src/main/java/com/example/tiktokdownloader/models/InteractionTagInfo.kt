package com.example.tiktokdownloader.models

data class InteractionTagInfo(
    val interest_level: Int,
    val tagged_users: List<TaggedUser>,
    val video_label_text: String
)