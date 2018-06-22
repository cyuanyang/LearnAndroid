package com.cyy.mylearnandroid.observe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.cyy.mylearnandroid.R
import kotlinx.android.synthetic.main.activity_observable.*
import java.lang.Exception

class ObservableActivity : AppCompatActivity(),Observer<String> {

    var textValue = "init"
    var count = 0

    val observale = Observable<String>().apply {
        this.addObserver(this@ObservableActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observable)

        valueTextView.text = textValue

        beginBtn.setOnClickListener({
            count += 1
            observale.notify(count.toString())
        })
    }

    override fun update(value: String?) {
        valueTextView.text = value
    }

    override fun onError(e: Exception?) {
    }



}
