package com.example.tiktokdownloader.Model

data class RiskInfos(
    val content: String,
    val risk_sink: Boolean,
    val type: Int,
    val vote: Boolean,
    val warn: Boolean
)