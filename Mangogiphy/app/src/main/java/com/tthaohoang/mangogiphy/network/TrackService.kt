package com.tthaohoang.mangogiphy.network

import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.VolleyError
import com.neopixl.spitfire.listener.RequestListener
import com.neopixl.spitfire.request.BaseRequest
import com.tthaohoang.mangogiphy.activity.LoginUser

class TrackService {
    companion object {
        fun getTracks(query: String) {
            var url = UrlBuilder.getTrackUrl() + "?q=" + query + "&index=0&limit=2"
            var request = BaseRequest
                    .Builder<TrackResult>(Request.Method.GET, url, TrackResult::class.java)
                    .listener(object: RequestListener<TrackResult> {
                        override fun onSuccess(request: Request<*>?, response: NetworkResponse?, result: TrackResult?) {
                            result?.results
                        }

                        override fun onFailure(request: Request<*>?, response: NetworkResponse?, error: VolleyError?) {
                            error?.networkResponse
                        }

                    })
            // envoi de la requête (appel WS) dans la requestQueue principale
            LoginUser.instance
                    .requestQueue.add(request.build())

        }

    }
}