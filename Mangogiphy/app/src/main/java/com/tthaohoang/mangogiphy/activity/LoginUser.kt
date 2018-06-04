package com.tthaohoang.mangogiphy.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.Volley
import com.deezer.sdk.model.Permissions
import com.deezer.sdk.network.connect.SessionStore
import com.deezer.sdk.network.connect.event.DialogListener
import com.tthaohoang.mangogiphy.MainActivity
import com.tthaohoang.mangogiphy.R
import com.tthaohoang.mangogiphy.data.DeezerManager
import java.lang.Exception

class LoginUser : AppCompatActivity() {

    companion object {
        lateinit var instance: LoginUser
    }

    lateinit var requestQueue: RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_user)


        login()

    }

    fun login() {
        val sessionStore = SessionStore()
        val deezerConnect = DeezerManager.deezerConnect
        val intent = Intent(this, MainActivity::class.java)

        if (sessionStore.restore(deezerConnect, this)) {
            // The restored session is valid, navigate to the Main Activity (gifs)
            startActivity(intent)
        } else {
            deezerConnect.authorize(
                    this,
                    arrayOf(Permissions.BASIC_ACCESS),
                    object : DialogListener {
                        override fun onComplete(p0: Bundle?) {
                            sessionStore.save(deezerConnect, this@LoginUser)
                            startActivity(intent)
                        }

                        override fun onCancel() {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onException(p0: Exception?) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    }
            )
        }
    }
}
