package com.hklee.musicplayer.ui.Activity

import android.os.Bundle
import androidx.activity.viewModels
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseActivity
import com.hklee.musicplayer.ui.viewmodel.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.lifecycle.Observer
import com.hklee.musicplayer.data.Song
import com.hklee.musicplayer.databinding.ActivityMainBinding
import com.hklee.musicplayer.ui.viewmodel.RepoModel

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    val viewModel by viewModels<RepoModel>()
    val playerViewModel by viewModels<PlayerViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        val viewModelObserver = Observer<Song> { song ->
            song?.let {
                playerViewModel.setSongAndPrepare(it.file)
                playerViewModel.setLyrics(it.lyrics)
            }
        }
        viewModel.currentSong.observe(this, viewModelObserver)
    }
}

