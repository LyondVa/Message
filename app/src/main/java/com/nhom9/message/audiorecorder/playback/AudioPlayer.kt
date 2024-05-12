package com.nhom9.message.audiorecorder.playback

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}