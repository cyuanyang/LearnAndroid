package com.cyy.okhttpsimple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cyy.okhttpsimple.Interceptor.*
import kotlinx.android.synthetic.main.activity_interceptor.*
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

inline fun _log(msg:String){
    Log.e("log" , msg)
}

class InterceptorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_interceptor)
        button.setOnClickListener {
            textView.text = ""
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

            val callback = object : Interceptor.ChainCallback{

                override fun proceedBefore(info:String) {
                    val txt = textView.text
                    textView.text = txt.toString().plus("\n").plus(info)
                }

                override fun proceedAfter(info:String) {
                    val txt = textView.text
                    textView.text = txt.toString().plus(" \n ").plus(info)
                }
            }

            var response = with(RealChain(interceptors , 0 , request , callback)){
                proceed(request)
            }

            _log(" response: "+ response.toString() + " request:"+response.requets.toString() )
        }
    }
}
