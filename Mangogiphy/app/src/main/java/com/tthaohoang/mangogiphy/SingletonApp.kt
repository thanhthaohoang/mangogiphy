package com.tthaohoang.mangogiphy

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class SingletonApp constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: SingletonApp? = null
        fun getInstance(context: Context) {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: SingletonApp(context)
            }
        }
    }
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}