package com.cyy.okhttpsimple

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLConnection
import java.util.*

class MainActivity : AppCompatActivity() {


    companion object {

        val client:OkHttpClient = OkHttpClient()
                .newBuilder()
                .cache(Cache(App.getApp().getExternalFilesDir("ok") , 1024*1024))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //http simple
        httpSimple.setOnClickListener {
            var request = Request.Builder()
                    .url("http://www.baidu.com")
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .build()

            Thread(Runnable {

                client.newCall(request).execute().use {
                    print(" body == ${it.body()!!.string()}")
                }

            }).start()
        }
        //interceptorSimple
        interceptorSimple.setOnClickListener {
            startActivity(Intent(this@MainActivity , InterceptorActivity::class.java))
        }
    }



}
