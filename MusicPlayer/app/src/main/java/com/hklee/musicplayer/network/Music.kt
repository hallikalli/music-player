package com.hklee.musicplayer.network

data class Music(
    val singer: String,
    val album: String,
    val title: String,
    val duration: Int,
    val image: String,
    val file: String,
    val lyrics: String,
    var current: Int
)