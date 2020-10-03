package com.hklee.musicplayer.ui.player.musicplayer

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseFragment
import com.hklee.musicplayer.databinding.FragmentMusicPlayerBinding
import com.hklee.musicplayer.ui.player.LyricAdapter
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
        }

        //ActionBar Setting
        activity?.let { it as AppCompatActivity }?.supportActionBar?.apply {
            this.title = ""
            this.subtitle = ""
        }

//        lvMusicPlayer.setLrcData(playerController.lyrics)
        sbMusicPlayer.setOnSeekBarChangeListener(playerController)
//        rvLyricMini.setOnTouchListener { view, motionEvent -> false }

        vFilmOfLycPlayerMini.setOnClickListener {
            val action =
                MusicPlayerFragmentDirections.toLyricPlayerFragment()
            findNavController().navigate(action)
        }
//        rvLyricMini.isNestedScrollingEnabled=false

        setAdapter()
    }


    fun setAdapter(){

        var adapter = LyricAdapter(R.style.LyricViewMini)
        rvLyricMini.adapter = adapter

        adapter.setData(playerController.lyrics2)
        adapter.notifyDataSetChanged()
        rvLyricMini.layoutManager = LinearLayoutManager(activity);


        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            (rvLyricMini.adapter as LyricAdapter).updateTime(pair.first.toLong());
            rvLyricMini.scrollToPosition(
                (rvLyricMini.adapter as LyricAdapter).currentLine+1)
        }
        playerController.nowPlaying.observe(this, playingObserver)

    }
}
