package com.example.mywebbrowser

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.view.menu.MenuAdapter
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.browse
import org.jetbrains.anko.email
import org.jetbrains.anko.sendSMS
import org.jetbrains.anko.share

var submenu: SubMenu? = null                        //서브메뉴를 사용하기 위해 전역변수 선언

class MainActivity : AppCompatActivity() {

    val favoritArray = HashMap<Int, String>()       //해쉬맵 선언
    var createMenu = false                         //메뉴가 추가 됬는지 확인하기 위한 bool변수
    var itemNum = 1                                 //해쉬맵키값, 즐겨찾기 아이템아이디를 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }

        //시작페이지를 구글로 설정
        webView.loadUrl("https://www.google.com")

        //URLedittext에 있는 값을 받아 이동하고 URLedittext 숨김
        urlEditText.setOnEditorActionListener { _, actionId, _ ->
            webView.loadUrl(urlEditText.text.toString())
            urlEditText.visibility = View.GONE
            true
        }
        registerForContextMenu(webView)
    }

    //뒤로가기 동작 재정의
    override fun onBackPressed(){
        if(webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    //옵션메뉴 리소스 지정
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        //즐겨찾기 목록 생성
        if (menu != null) {
            submenu = menu.addSubMenu("즐겨찾기 목록") as SubMenu
        }

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if( createMenu ) {              //즐겨찾기 추가 했는지 안했는지 검사
            if( menu != null) {          //메뉴에 값이 제대로 들어왔는지 검사
                submenu!!.add(0, itemNum, Menu.NONE, "즐겨찾기 ${itemNum++}")   //즐겨찾기 목록에 추가
                createMenu = false
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    //옵션 메뉴의 처리
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            R.id.action_chosun -> {
                webView.loadUrl("https://m.gamechosun.co.kr")
                return true
            }
            R.id.action_google, R.id.action_home -> {
                webView.loadUrl("https://www.google.com")
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl("https://www.naver.com")
                return true
            }
            R.id.action_daum -> {
                webView.loadUrl("https://www.daum.net")
                return true
            }

            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:010-2579-4475")
                if(intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                //문자보내기
                sendSMS("010-2579-4475", webView.url)
                return true
            }
            R.id.action_email -> {
                //이메일 보내기
                email("baduk0425@gmail.com", "좋은 사이트", webView.url)
                return true
            }

            R.id.favorit_add -> {
                favoritArray.put(itemNum, webView.getUrl())     //해쉬맵에 키값과 내용 넣음.
                createMenu = true                                //추가 되었음을 알려줌
                return true
            }
            R.id.action_search -> {
                if( urlEditText.visibility == View.GONE )
                    urlEditText.visibility = View.VISIBLE
                else
                    urlEditText.visibility = View.GONE
            }
        }

        if( item != null) {                                                             //값이 있는지 검사
            if( favoritArray.get(item.itemId) != null ) {                             //해쉬맵에 값이 있는지 검사
                when(item?.itemId) {
                    item.itemId -> webView.loadUrl(favoritArray.get(item.itemId))      //해쉬맵에서 아이템값에 해당하는 위치에서 URL가져와서 이동
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //컨텍스트 메뉴 작성
    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context, menu)
    }

    //컨텍스트 메뉴 클릭 이벤트 처리
    override fun onContextItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.action_share -> {
                //페이지 공유
                share(webView.url)
                return true
            }
            R.id.action_browser -> {
                //기본 웹 브라우저에서 열기
                browse(webView.url)
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}
