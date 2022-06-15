package com.example.tiktokdownloader.Model

data class Cha(
    val author: AuthorX,
    val banner_list: Any,
    val cha_attrs: Any,
    val cha_name: String,
    val cid: String,
    val collect_stat: Int,
    val connect_music: List<Any>,
    val desc: String,
    val extra_attr: ExtraAttr,
    val hashtag_profile: String,
    val is_challenge: Int,
    val is_commerce: Boolean,
    val is_pgcshow: Boolean,
    val schema: String,
    val search_highlight: Any,
    val share_info: ShareInfoX,
    val show_items: Any,
    val sub_type: Int,
    val type: Int,
    val user_count: Int,
    val view_count: Int
)