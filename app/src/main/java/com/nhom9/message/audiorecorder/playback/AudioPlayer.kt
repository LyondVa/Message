package com.nhom9.message.audiorecorder.playback

import java.io.File

interface AudioPlayer {
    fun playFromUrl(url: String)
    fun resume()
    fun pause()
    fun stop()
    fun getCurrentPosition():Int
}