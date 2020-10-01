package com.hklee.musicplayer.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseActivity
import com.hklee.musicplayer.databinding.ActivityMusicPlayBinding
import com.hklee.musicplayer.ui.player.PlayerController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_music_player.*
import javax.inject.Inject
import androidx.lifecycle.Observer
import com.hklee.musicplayer.data.Song
import com.hklee.musicplayer.ui.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMusicPlayBinding>(
    R.layout.activity_music_play
) {
    @Inject
    lateinit var playerController: PlayerController
    val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        val viewModelObserver = Observer<Song> { song ->
            song?.file?.let {
                playerController.setNewTrack(it)
            }
            song?.let {
                playerController.setLyrics(it.lyrics)
            }
        }
        viewModel.playerStatus.observe(this, viewModelObserver)


    }

}

