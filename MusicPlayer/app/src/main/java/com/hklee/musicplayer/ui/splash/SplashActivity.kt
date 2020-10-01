package com.hklee.musicplayer.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.hklee.musicplayer.ui.MainActivity

class SplashActivity : AppCompatActivity() {
    private val SPLASH_DISPLAY_TIME: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, SPLASH_DISPLAY_TIME);
    }
}