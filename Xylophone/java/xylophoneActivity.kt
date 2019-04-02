package com.example.xylophone

import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_piano.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class xylophoneActivity : AppCompatActivity() {
    //내가 녹음한 키와 거기에 해당하는 soundId값 저장용 hashmap
    private val soundMap = HashMap<Pair<Int, Int>, Int>()

    //녹을중인지 아닌지 확인을 위한 boolean변수
    private var isRecord = false
    //녹음중에 어떤 키를 눌렀는지 값 저장용 ArrayList
    private var clickList = ArrayList<Pair<Int, Int>>()
    //키와 키 사이에 걸린 시간 저장용 list
    private var timeList = mutableListOf<Long>()

    //시간 계산을 위한 변수
    private var time1: Date? = null
    private var time2: Date? = null

    private val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //내 버전이 롤리팝과 같거나 높을 때
        //건반이 총 8개여서 8개로 초기화
        SoundPool.Builder().setMaxStreams(8).build()
    } else {
        //롤리팝보다 버전이 낮을 때 초기화 하는 법
        SoundPool(8, AudioManager.STREAM_MUSIC, 0)
    }

    //건반마다 필요한 소리를 연결해주는 list
    private val sounds = listOf(
        Pair(R.id.do1, R.raw.do1),
        Pair(R.id.re1, R.raw.re),
        Pair(R.id.mi, R.raw.mi),
        Pair(R.id.fa1, R.raw.fa),
        Pair(R.id.sol1, R.raw.sol),
        Pair(R.id.la, R.raw.la),
        Pair(R.id.si, R.raw.si),
        Pair(R.id.do2, R.raw.do2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        //가로 모드로 고정
        //manifest에서 intent-filter위에 옵션추가
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xylophone)

        //건반이 선택 되었을 때 소리가 나게 초기화
        sounds.forEach { tune(it) }

        recordButton.setOnClickListener {
            if( isRecord ){
                //녹음중에 버튼이 눌리면 녹음을 멈춤
                isRecord = false
            } else {
                //처음 녹음버튼이 눌렸을 때 시간을 가져옴
                time1 = Date()
                //녹음중이 아닐때 버튼이 눌리면 녹음 시작
                isRecord = true
            }
        }

        playButton.setOnClickListener {
            //timelist에 있는 값을 가져오기위한 변수
            var i = 0
            //clicklist에 값이 있으면 s에 값을 넣고 for문 실행
            for(s in clickList) {
                //키와 키 사이에 걸린 시간을 가져옴
                var time = timeList[i++]
                //키와 키 사이에 걸린 시간만큼 멈춤
                Thread.sleep(time)
                //누른 키 값에 해당하는 소리 실행
                start(s)
            }
        }

        stopButton.setOnClickListener {
            //지금까지 녹음에 들어갔던 키값 초기화
            clickList = ArrayList<Pair<Int, Int>>()
            //녹음을 중지함
            isRecord = false
        }
    }

    private fun tune(pitch: Pair<Int, Int>) {
        //키가 눌렸을 때 해당하는 음악파일 가져와서 soundId값에 넣기
        val soundId = soundPool.load(this, pitch.second, 1)
        //키와 키에 해당하는 soundId값 추가
        soundMap.put(pitch, soundId)
        findViewById<TextView>(pitch.first).setOnClickListener {
            //녹음을 시작했을 때
            if( isRecord ) {
                //현재 시간을 가져옴
                time2 = Date()
                //현재 시간에서 전에 저장한 시간 빼서 timelist에 추가
                timeList.add((time2!!.time - time1!!.time))
                //이전 시간에 현재시간을 넣음
                time1 = time2
                //내가 어떤키를 눌렀는지 clicklist에 저장
                clickList.add(pitch)
            }

            //내가 누른 키 값에 해당하는 소리를 재생
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }

    private fun start(s: Pair<Int, Int>) {
        //내가 누른 키 값에 해당하는 soundId값 가져오기
        var soundId  = soundMap.get(s) as Int
        //해당되는 사운드 플레이
        soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
    }
}
