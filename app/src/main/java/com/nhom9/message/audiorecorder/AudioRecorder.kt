package com.nhom9.message.audiorecorder

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}