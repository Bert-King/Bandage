package com.panda912.bandage.sample

import android.app.Activity
import android.os.Bundle
import android.widget.Button

class MainActivity : Activity() {
    var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_divide).setOnClickListener {
            println(10 / 0)
        }

        findViewById<Button>(R.id.btn_npe).setOnClickListener {
            println(address!!.length)
        }
    }
}