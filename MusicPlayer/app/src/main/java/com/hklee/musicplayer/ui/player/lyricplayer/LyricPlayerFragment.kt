package com.hklee.musicplayer.ui.player.lyricplayer

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseFragment
import com.hklee.musicplayer.databinding.FragmentLyricPlayerBinding
import com.hklee.musicplayer.ui.player.PlayerController
import com.hklee.musicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_lyric_player.*
import javax.inject.Inject

@AndroidEntryPoint
class LyricPlayerFragment :
    BaseFragment<FragmentLyricPlayerBinding, MainViewModel>(R.layout.fragment_lyric_player) {
    override val viewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var playerController: PlayerController

    override fun init() {
        with(binding) {
            vm = viewModel
            mc = playerController
        }.also {
            println("LyricPlayerFragment : 완료 + ${playerController.hashCode()}" + "${viewModel.hashCode()}")
        }


        lvLyricPlayer.setLrcData(playerController.lyrics)
        sbLyricPlayer.setOnSeekBarChangeListener(playerController)

        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            lvLyricPlayer.updateTime(pair.first.toLong());
        }
        playerController.nowPlaying.observe(this, playingObserver)
    }
}