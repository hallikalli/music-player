package com.hklee.musicplayer.ui

import android.os.Bundle
import androidx.activity.viewModels
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseActivity
import com.hklee.musicplayer.ui.player.PlayerController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.hklee.musicplayer.data.Song
import com.hklee.musicplayer.databinding.ActivityMainBinding
import com.hklee.musicplayer.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    @Inject
    lateinit var playerController: PlayerController
    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
/*  *//*      supportActionBar?.apply {
//            setDisplayShowTitleEnabled(false)
//            setDisplayShowHomeEnabled(false)
            setBackgroundDrawable(object: ColorDrawable(Color.TRANSPARENT))
        }*//*
        supportActionBar
        supportActionBar?.title=""*/
//        supportActionBar?.title="asdf"
        val viewModelObserver = Observer<Song> { song ->
            song?.file?.let {
                playerController.setNewSong(it)
            }
            song?.let {
                playerController.setLyrics(it.lyrics)
            }
        }
//        supportActionBar.set
        viewModel.currentSong.observe(this, viewModelObserver)


    }

}

