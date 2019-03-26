package com.example.mygallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import kotlinx.android.synthetic.main.activity_gallery.*
import kotlin.concurrent.timer

class GalleryActivity : AppCompatActivity() {

    var galleryName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        galleryName = intent.getStringExtra("galleryName")

        getAllPhotos()
    }

    private fun getAllPhotos() {
        //모든 사진 정보 가져오기
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            "bucket_display_name LIKE '%$galleryName%'",
            null,
            MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC")    //찍은 날짜 내림차순

        val fragments = ArrayList<Fragment>()
        if( cursor != null ) {
            while(cursor.moveToNext()) {
                //사진 경로 Uri 가져오기
                val uri = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                fragments.add(PhotoFragment.newInstance(uri))
            }
            cursor.close()
        }

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.updateFragments(fragments)
        viewPager.adapter = adapter

        timer(period = 3000) {
            runOnUiThread {
                if( viewPager.currentItem < adapter.count - 1) {
                    viewPager.currentItem = viewPager.currentItem + 1
                } else {
                    viewPager.currentItem = 0
                }
            }
        }
    }
}
