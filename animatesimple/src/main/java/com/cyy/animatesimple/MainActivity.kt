package com.cyy.animatesimple

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cyy.animatesimple.svganimate.SVGAnimations
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svgAnimBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity , SVGAnimations::class.java))
        }

    }
}
