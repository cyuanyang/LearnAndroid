package com.cyy.animatesimple

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cyy.animatesimple.animator.CustomEvaluator
import com.cyy.animatesimple.svganimate.SVGAnimations
import com.cyy.animatesimple.animator.PathAnimations
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        svgAnimBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity , SVGAnimations::class.java))
        }

        pathAnimBtn.setOnClickListener {
            startActivity(Intent(this , PathAnimations::class.java))
        }

        customEvaluator.setOnClickListener {  startActivity(Intent(this , CustomEvaluator::class.java)) }

    }
}
