package com.cyy.nestscroll

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.eeeBtn).setOnClickListener({
            startActivity(Intent(this@MainActivity , Main2Activity::class.java))
        })
    }
}
