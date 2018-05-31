package com.tthaohoang.mangogiphy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.deezer.sdk.network.connect.DeezerConnect
import com.deezer.sdk.player.TrackPlayer
import com.deezer.sdk.player.event.PlayerState
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker
import com.tthaohoang.mangogiphy.R
import com.tthaohoang.mangogiphy.data.DeezerManager
import kotlinx.android.synthetic.main.activity_deezer_suggest.*


class DeezerSuggest : AppCompatActivity() {

    lateinit var deezerConnect: DeezerConnect
    lateinit var trackPlayer: TrackPlayer
    private var query: String = ""
    private var trackId: Long = 12565420

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deezer_suggest)

        query = intent.getStringExtra("EXTRA_QUERY")

        //récupère les tracks en fonction de la recherche
        deezerConnect = DeezerManager.deezerConnect

        trackPlayer = TrackPlayer(application, deezerConnect, WifiAndMobileNetworkStateChecker())
        playTrack()

        playBtn.setOnClickListener({ togglePlay() })

    }

    // init player track from deezer
    private fun playTrack() {
//        albumPlayer.playAlbum(albumId)
//        TrackService.getTracks(query)
        trackPlayer.playTrack(trackId)
    }

    override fun onDestroy() {
        // stop music when activity is left
        trackPlayer.stop()
        trackPlayer.release()
        super.onDestroy()
    }

    // play or stop the music by clicking on the button player
    fun togglePlay() {
        if(isPlaying()) {
            trackPlayer.pause()
            playBtn.setImageResource(R.drawable.stop_button)
        } else {
            trackPlayer.play()
            playBtn.setImageResource(R.drawable.play_button)
        }
    }

    //check the state of the music
    // playing or not ?
    private fun isPlaying(): Boolean {
        return when(trackPlayer.playerState) {
            PlayerState.PLAYING, PlayerState.STARTED ->
                    true
            else ->
                    false
        }
    }
}
