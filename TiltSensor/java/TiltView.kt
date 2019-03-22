package com.example.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.util.Log
import android.view.View

class TiltView(context: Context?) : View(context) {
    private val greenPaint : Paint = Paint()
    private val blackPaint: Paint = Paint()
    private val bluePaint: Paint = Paint()

    private var cX: Float = 0f
    private var cY: Float = 0f

    private var xCoord: Float = 0f
    private var yCoord: Float = 0f

    private var RadianX = 0.0
    private var RadianY = 0.0

    fun onSensorEvent(event: SensorEvent) {
        yCoord = event.values[0] * 20
        xCoord = event.values[1] * 20

        RadianX = Math.atan2(event.values[0].toDouble(),  event.values[2].toDouble()) * 180/Math.PI
        RadianY = Math.atan2(event.values[1].toDouble(),  event.values[2].toDouble()) * 180/Math.PI

        invalidate()
    }

    init {
        greenPaint.color = Color.GREEN
        bluePaint.color = Color.CYAN
        blackPaint.style = Paint.Style.STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cX = w / 2f
        cY = h / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        onCircle(canvas)
        //onbar(canvas)
    }

    fun onCircle(canvas: Canvas?) {
        //수평이 맞았을 경우 배경색 변경
        if( xCoord == 0f && yCoord == 0f)
            canvas?.drawPaint(bluePaint)
        //바깥 원
        canvas?.drawCircle(cX, cY, 100f, blackPaint)
        //녹색 원
        canvas?.drawCircle(xCoord + cX, yCoord + cY, 100f, greenPaint)
        //가운데 십자가
        canvas?.drawLine(cX - 20, cY, cX + 20, cY, blackPaint)
        canvas?.drawLine(cX, cY - 20, cX, cY + 20, blackPaint)

        blackPaint.setTextSize(100f);
        if( RadianX > RadianY ) {
            if( RadianX.toInt() < 10 )
                canvas?.drawText("${RadianX.toInt()}", cX - 30, cY + 35, blackPaint)
            else
                canvas?.drawText("${RadianX.toInt()}", cX - 55, cY + 35, blackPaint)
        } else {
            if( RadianY.toInt() < 10 )
                canvas?.drawText("${RadianY.toInt()}", cX - 30, cY + 35, blackPaint)
            else
                canvas?.drawText("${RadianY.toInt()}", cX - 55, cY + 35, blackPaint)
        }
    }

    fun onbar(canvas: Canvas?) {
        //파란 밑배경
        canvas?.drawRect(0f, yCoord + cY, cX * 2, cY * 2, bluePaint)
        //가운데 막대기
        canvas?.drawLine(cX - 50, cY, cX + 50, cY, blackPaint)
    }
}