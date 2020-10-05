package com.hklee.musicplayer.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hklee.musicplayer.R

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //지 2초간 스플래시 유지
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, SPLASH_DISPLAY_TIME);
    }
}