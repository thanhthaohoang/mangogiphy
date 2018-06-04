package com.tthaohoang.mangogiphy.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.deezer.sdk.model.Track
import com.deezer.sdk.network.connect.DeezerConnect
import com.deezer.sdk.network.request.DeezerRequestFactory
import com.deezer.sdk.network.request.event.JsonRequestListener
import com.deezer.sdk.network.request.event.RequestListener
import com.deezer.sdk.player.TrackPlayer
import com.deezer.sdk.player.event.PlayerState
import com.deezer.sdk.player.networkcheck.WifiAndMobileNetworkStateChecker
import com.tthaohoang.mangogiphy.R
import com.tthaohoang.mangogiphy.data.DeezerManager
import com.tthaohoang.mangogiphy.model.TrackList
import kotlinx.android.synthetic.main.activity_deezer_suggest.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception


@Suppress("CAST_NEVER_SUCCEEDS")
class DeezerSuggest : AppCompatActivity() {

    lateinit var deezerConnect: DeezerConnect
    lateinit var trackPlayer: TrackPlayer
    private var query: String = ""
    private var trackId: Long = 12565420
    private var tracksList = arrayListOf<Track>()
    private lateinit var listener: RequestListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //hide top bar
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_deezer_suggest)


        //stop my activity/ dialog when the user clicks outside of the dialog
        this.setFinishOnTouchOutside(true)


        query = intent.getStringExtra("EXTRA_QUERY")

        //récupère les tracks en fonction de la recherche
        deezerConnect = DeezerManager.deezerConnect

        trackPlayer = TrackPlayer(application, deezerConnect, WifiAndMobileNetworkStateChecker())
        trackPlayer.addOnPlayerProgressListener {
            showPlayerProgress(it.toInt())
        }

        searchTracks(query)

        playBtn.setOnClickListener({ togglePlay() })

    }

    fun searchTracks(query: String) {
        listener = object: JsonRequestListener() {
            override fun onResult(result: Any?, requestId: Any?) {
                tracksList.clear()
                if (result is ArrayList<*>) {
                    for (element in result) {
                        tracksList.add(element as Track)
                    }
                    tracksList.shuffle()
                    val randomTrack = tracksList[0]
                    playTrack(randomTrack)

                }
            }

            override fun onUnparsedResult(p0: String?, p1: Any?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onException(p0: Exception?, p1: Any?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        val request = DeezerRequestFactory.requestSearchTracks(query)
        deezerConnect.requestAsync(request, listener)
    }

    // init player track from deezer
    fun playTrack(track: Track) {
        songTitle.text = track.title
        artistTextView.text = track.artist.name
        showPlayerProgress(0)
        Glide
                .with(this)
                .load(track.album.imageUrl)
                .into(albumCover)
        trackPlayer.playTrack(track.id)

    }

    fun showPlayerProgress(timePosition: Int) {
        progressBar.progress = timePosition
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
            playBtn.setImageResource(R.drawable.play_button)
        } else {
            trackPlayer.play()
            playBtn.setImageResource(R.drawable.stop_button)
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
