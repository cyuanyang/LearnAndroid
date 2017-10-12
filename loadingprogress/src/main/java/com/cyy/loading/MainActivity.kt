package com.cyy.loading

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.cyy.progress.loading.LoadingProgress
import com.cyy.progress.loading.ProgressListener

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        headerLayout.setOnClickListener {
            loadingProgress.start()

            headerLayout.postDelayed({
                loadingProgress.reset()
            } , 7000)
        }

        loadingProgress.listener = object :ProgressListener{
            override fun onProgressStartListener(progress: LoadingProgress) {
                Log.i("ProgressListener1" , "start")
            }

            override fun onProgressRepeatListener(progress: LoadingProgress) {
                Log.i("ProgressListener1" , "repeat")
            }

            override fun onProgressResetListener(progress: LoadingProgress) {
                Log.i("ProgressListener1" , "reset")
            }

            override fun onProgressEndListener(progress: LoadingProgress) {
                Log.i("ProgressListener1" , "end")
            }

        }


        headerLayout2.setOnClickListener {
            loadingProgress2.start()

            headerLayout2.postDelayed({
                loadingProgress2.reset()
            } , 7000)
        }

        loadingProgress2.listener = object :ProgressListener{
            override fun onProgressStartListener(progress: LoadingProgress) {
                Log.i("ProgressListener2" , "start")
            }

            override fun onProgressRepeatListener(progress: LoadingProgress) {
                Log.i("ProgressListener2" , "repeat")
            }

            override fun onProgressResetListener(progress: LoadingProgress) {
                Log.i("ProgressListener2" , "reset")
            }

            override fun onProgressEndListener(progress: LoadingProgress) {
                Log.i("ProgressListener2" , "end")
            }

        }
    }
}

