package com.hklee.musicplayer.ui.fragment

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseFragment
import com.hklee.musicplayer.databinding.FragmentMusicPlayerBinding
import com.hklee.musicplayer.ui.adapter.LyricAdapter
import com.hklee.musicplayer.ui.viewmodel.PlayerViewModel
import com.hklee.musicplayer.ui.viewmodel.RepoModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_music_player.*


@AndroidEntryPoint
class MusicPlayerFragment :
    BaseFragment<FragmentMusicPlayerBinding, RepoModel>(R.layout.fragment_music_player) {
    override val mainViewModel by activityViewModels<RepoModel>()
    private val playerViewModel by activityViewModels<PlayerViewModel>()

    override fun init() {
        with(binding) {
            vm = mainViewModel
            mc = playerViewModel
        }

        initPlyerBar()
        initLyricView()
    }

    private fun initPlyerBar() {
        sbMusicPlayer.setOnSeekBarChangeListener(playerViewModel)
    }

    fun initLyricView() {
        var adapter = LyricAdapter(playerViewModel.lyrics, R.style.LyricViewMini)
        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            (rvLyricMini.adapter as LyricAdapter).updateTime(pair.first.toLong());
            rvLyricMini.scrollToPosition(
                (rvLyricMini.adapter as LyricAdapter).currentLine + 1
            )
        }
        rvLyricMini.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(activity);
        }

        playerViewModel.playingProgress.observe(this, playingObserver)
        vFilmOfLycPlayerMini.setOnClickListener {
            val action =
                MusicPlayerFragmentDirections.toLyricPlayerFragment()
            findNavController().navigate(action)
        }
    }
}
