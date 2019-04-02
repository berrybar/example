package com.example.xylophone

import android.content.Intent
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pianoButton.setOnClickListener {
            val intent = Intent(this, pianoActivity::class.java)
            startActivity(intent)
        }
        xylophoneButton.setOnClickListener {
            val intent = Intent(this, xylophoneActivity::class.java)
            startActivity(intent)
        }
    }
}
