package com.tthaohoang.mangogiphy

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log
import android.view.View
import android.widget.SearchView
import com.giphy.sdk.core.models.enums.MediaType
import com.giphy.sdk.core.network.api.GPHApiClient
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter
import com.mikepenz.fastadapter.listeners.OnClickListener
import com.tthaohoang.mangogiphy.activity.DeezerSuggest
import com.tthaohoang.mangogiphy.data.GiphyManager
import com.tthaohoang.mangogiphy.item.GifItem
import com.tthaohoang.mangogiphy.item.MoodItem
import com.tthaohoang.mangogiphy.model.Gif
import com.tthaohoang.mangogiphy.model.Mood
import com.tthaohoang.mangogiphy.presentation.SpacingItemDecoration
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var gifsItemAdapter: FastItemAdapter<GifItem>
    private lateinit var moodsItemAdapter: FastItemAdapter<MoodItem>
    lateinit var client: GPHApiClient
    lateinit var queryTag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Moods
        val listMoods = arrayListOf<Mood>()
        listMoods.add(Mood("Happy", R.drawable.mood_happy))
        listMoods.add(Mood("Sad", R.drawable.mood_sad))
        listMoods.add(Mood("Morning", R.drawable.mood_morning))

        moodsRecyclerView.layoutManager = LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false)
        moodsRecyclerView.addItemDecoration(SpacingItemDecoration(5))
        moodsItemAdapter = FastItemAdapter<MoodItem>()
        moodsRecyclerView.adapter = moodsItemAdapter

        for (mood in listMoods) {
            val moodItem = MoodItem(mood)
            moodsItemAdapter.add(moodItem)
        }

        moodsItemAdapter.withOnClickListener(object: OnClickListener<MoodItem> {
            override fun onClick(v: View?, adapter: IAdapter<MoodItem>?, item: MoodItem?, position: Int): Boolean {
                if (item != null) {
                    // search for specific mood
                    searchBar.setQuery(item.getLabel(), true)
                    searchBar.clearFocus()
                }
                return true
            }
        })

        // relier le linearlayoutmanager au recycler view
        gifsRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        gifsRecyclerView.addItemDecoration(SpacingItemDecoration(5))
        gifsItemAdapter = FastItemAdapter<GifItem>()

        // relier le fastadapter au recycler view
        gifsRecyclerView.adapter = gifsItemAdapter

        // open search bar directly
        searchBar.setIconifiedByDefault(false)

        client = GiphyManager.client
        queryTag = ""

        // search gifs when user add a request in search bar
        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                on submit, fetch gifs
                if (query is String) {
                    searchGifs(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        // play track when a gif is clicked
        val intent = Intent(this, DeezerSuggest::class.java)

        //gestion du click event
        gifsItemAdapter.withOnClickListener(object: OnClickListener<GifItem> {
            override fun onClick(v: View?, adapter: IAdapter<GifItem>?, item: GifItem?, position: Int): Boolean {
                val gif = item?.gif

                if (gif is Gif) {
                    // show Deezer suggestion
                    intent.putExtra("EXTRA_QUERY", queryTag)
                    startActivity(intent)
                }
                return true
            }
        })
    }

    fun searchGifs(query: String) {
        var arrayGifs = arrayListOf<Gif>()
        queryTag = query
//                on submit, fetch gifs
        arrayGifs.clear()
        gifsItemAdapter.clear()
        gifsItemAdapter.notifyDataSetChanged()

        client.search(queryTag, MediaType.gif, 5, 0, null, null) { result, e ->
            if (result == null) {
                val errorMsg = "No results for $queryTag"
                queryTextView.text = errorMsg
            } else {
                if (result.data != null) {

                    val successMsg = "Results for $queryTag"
                    queryTextView.text = successMsg

//                                add gifs in arraylistof Gif model
                    for (gif in result.data) {
//                                    Log.v("giphy", gif.id)
                        arrayGifs.add(Gif(gif.images.fixedHeight.gifUrl))
                    }

//                                Display gifs in adapter
                    for (gif in arrayGifs) {
                        val gifItem = GifItem(gif)
                        gifsItemAdapter.add(gifItem)
                    }
                } else {
                    Log.e("giphy error", "No results found")
                }
            }
        }
    }

}
