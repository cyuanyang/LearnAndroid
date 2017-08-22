package com.cyy.okhttpsimple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cyy.okhttpsimple.Interceptor.*
import kotlinx.android.synthetic.main.activity_interceptor.*

inline fun _log(msg:String){
    Log.e("log" , msg)
}

class InterceptorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interceptor)
        button.setOnClickListener {
            //开始
            val interceptors:MutableList<Interceptor> = mutableListOf(
                    FirstInterceptor(),
                    SecondInterceptor(),
                    LastInterceptor()
            )

            val request = Request().apply {
                body = "123"
                url = "www.aaaa.com"
            }

            var response = with(RealChain(interceptors , 0 , request)){
                proceed(request)
            }
            _log(" response: "+ response.toString() + " request:"+response.requets.toString() )
        }
    }
}
