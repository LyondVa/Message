package com.nhom9.message.audiorecorder.playback

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.core.net.toUri
import java.io.File

class AndroidAudioPlayer(private val context: Context) : AudioPlayer {
    private var player: MediaPlayer? = null
    override fun playFromUrl(url: String) {
        MediaPlayer.create(context, Uri.parse(url)).apply {
            player = this
            start()
        }
    }

    override fun resume() {
        player?.start()
    }

    override fun pause() {
        player?.pause()
    }
    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }

    override fun getCurrentPosition(): Int {
        return player?.currentPosition!!
    }
}
