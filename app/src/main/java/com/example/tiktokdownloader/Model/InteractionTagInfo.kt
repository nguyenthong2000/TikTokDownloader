package com.example.tiktokdownloader.Model

data class InteractionTagInfo(
    val interest_level: Int,
    val tagged_users: List<TaggedUser>,
    val video_label_text: String
)