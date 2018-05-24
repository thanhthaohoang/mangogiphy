package com.tthaohoang.mangogiphy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.giphy.sdk.core.models.enums.MediaType
import com.tthaohoang.mangogiphy.data.GiphyManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//      get client from singleton GiphyManager
        val client = GiphyManager.client

        client.search("cats", MediaType.gif, 25, null, null, null) { result, e ->
            if (result == null) {
                // Do what you want to do with the error
            } else {
                if (result.data != null) {
                    var content = ""
                    for (gif in result.data) {
                        Log.v("giphy", gif.id)
                        Glide
                            .with(this)
                            .load(gif.images.fixedWidth.gifUrl)
                            .into(gifImage)
                    }
                } else {
                    Log.e("giphy error", "No results found")
                }
            }
        }
    }

}
