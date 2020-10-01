package com.hklee.musicplayer.ui.player.musicplayer

import android.view.View.OnTouchListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseFragment
import com.hklee.musicplayer.databinding.FragmentMusicPlayerBinding
import com.hklee.musicplayer.ui.player.PlayerController
import com.hklee.musicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_music_player.*
import javax.inject.Inject


@AndroidEntryPoint
class MusicPlayerFragment :
    BaseFragment<FragmentMusicPlayerBinding, MainViewModel>(R.layout.fragment_music_player) {
    override val viewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var playerController: PlayerController

    override fun init() {
        with(binding) {
            vm = viewModel
            mc = playerController
        }.also {
            println("MusicPlayerFragment : 완료 + ${playerController.hashCode()}" + "${viewModel.hashCode()}")
        }

        lvMusicPlayer.setLrcData(playerController.lyrics)
        sbMusicPlayer.setOnSeekBarChangeListener(playerController)

        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            lvMusicPlayer.updateTime(pair.first.toLong());
        }
        playerController.nowPlaying.observe(this, playingObserver)


        lvMusicPlayer.setOnTouchListener(OnTouchListener { _, _ -> false })
        lvMusicPlayer.setOnClickListener {
            val action =
                MusicPlayerFragmentDirections.toLyricPlayerFragment()
            findNavController().navigate(action)
        }
    }
}