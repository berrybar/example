package com.example.flashlight

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.util.*
import kotlin.concurrent.timer

class TorchService : Service() {
    private val torch: Torch by lazy {
        Torch(this)
    }

    private var isRunning = false
    private var timerTask: Timer? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            //앱에서 실행할 경우
            "on" -> {
                torch.flashOn()
                isRunning = true
            }
            "off" -> {
                torch.flashOff()
                isRunning = false
            }
            "flicker" -> {
                startFlicker()
            }
            "finish" -> {
                stopFlicker()
            }
            "refresh" -> {
                stopFlicker()
                startFlicker()
            }

            //서비스에서 실행할 경우
            else -> {
                isRunning = !isRunning
                if(isRunning) {
                    torch.flashOn()
                } else {
                    torch.flashOff()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopFlicker()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun startFlicker() {
        timerTask = timer(period = 1000 - seekTime) {
            if(isRunning) {
                torch.flashOff()
                isRunning = false
            } else {
                torch.flashOn()
                isRunning = true
            }
        }
    }

    private fun stopFlicker() {
        timerTask?.cancel()
        torch.flashOff()
        isRunning = false
    }
}
