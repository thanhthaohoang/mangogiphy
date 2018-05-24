package com.tthaohoang.mangogiphy

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.giphy.sdk.core.models.enums.MediaType
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.OnClickListener
import com.tthaohoang.mangogiphy.data.GiphyManager
import com.tthaohoang.mangogiphy.item.GifItem
import com.tthaohoang.mangogiphy.model.Gif
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // relier le linearlayoutmanager au recycler view
//        gifsRecyclerView.layoutManager = LinearLayoutManager(this,
//                LinearLayoutManager.INVALID_OFFSET, false)
        gifsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val gifsItemAdapter = FastItemAdapter<GifItem>()

        // relier le fastadapter au recycler view
        gifsRecyclerView.adapter = gifsItemAdapter

//      get client from singleton GiphyManager
        val client = GiphyManager.client

        var arrayGifs = arrayListOf<Gif>()

        client.search("cats dogs", MediaType.gif, 5, null, null, null) { result, e ->
            if (result == null) {
                // Do what you want to do with the error
            } else {
                if (result.data != null) {
                    var content = ""
                    for (gif in result.data) {
                        Log.v("giphy", gif.id)

                        arrayGifs.add(Gif(gif.images.fixedHeight.gifUrl))
                    }

                    for(gif in arrayGifs) {
                        val gifItem = GifItem(gif)
                        gifsItemAdapter.add(gifItem)
                    }
                } else {
                    Log.e("giphy error", "No results found")
                }
            }
        }

        //gestion du click event
        gifsItemAdapter.withOnClickListener(object: OnClickListener<GifItem> {
            override fun onClick(v: View?, adapter: IAdapter<GifItem>?, item: GifItem?, position: Int): Boolean {
                val gif = item?.gif

                if(gif is Gif) {
                    Toast
                            .makeText(this@MainActivity,
                                    gif.urlImg,
                                    Toast.LENGTH_SHORT)
                            .show()
                }
                return true
            }
        })
    }

}
