package com.example.flashlight

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

var seekTime : Long = 0
var isAble : Boolean = false

class MainActivity : AppCompatActivity() {

    private var isAlive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val torch = Torch(this)
        val intent = Intent(this, TorchService::class.java)

        timeSeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekTime = progress.toLong() * 100
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if( isAlive ) {
                    intent.setAction("refresh")
                    startService(intent)
                }
            }
        })

        flashSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                intent.setAction("on")
                startService(intent)
            } else {
                intent.setAction("off")
                startService(intent)
            }
        }

        startButton.setOnClickListener {
            if( isAlive ) {
                intent.setAction("finish")
                startService(intent)
                isAlive = false
            } else {
                intent.setAction("flicker")
                startService(intent)
                isAlive = true
            }
        }
    }
}
