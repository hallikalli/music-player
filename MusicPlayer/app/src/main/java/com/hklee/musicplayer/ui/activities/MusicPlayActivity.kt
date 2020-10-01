package com.hklee.musicplayer.ui.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseActivity
import com.hklee.musicplayer.databinding.ActivityMusicPlayBinding
import com.hklee.musicplayer.network.MainViewModel
import com.hklee.musicplayer.network.Song
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_music_play.*
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class MusicPlayActivity : BaseActivity<ActivityMusicPlayBinding>(
    R.layout.activity_music_play
) {
    private val mainViewModel: MainViewModel by viewModels()

    //    @Inject lateinit
    private val mediaController: MediaController = MediaControllerImpl()

    private val isTracking = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = mainViewModel
        binding.mediaController = mediaController
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayShowHomeEnabled(false)
        }

        val songObserver = Observer<Song> { song ->
            song?.file?.let { mediaController.setNewTrack(it) }
            song?.let { mediaController.setLyrics(song.lyrics) }
        }

        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            lrcView.updateTime(pair.first.toLong());
        }




        lrcView.setLrcData(mediaController.lyrics)
//        lrcView.setOnPlayIndicatorLineListener(mediaController)
        //Seekbar 리스너
        seekBar.setOnSeekBarChangeListener(mediaController)

        mediaController.nowPlaying.observe(this, playingObserver)

        mainViewModel.songStatusLiveData.observe(this, songObserver)

        lrcView.setOnTouchListener(OnTouchListener { v, event ->
            val action = event.action
            val curX = event.x
            val curY = event.y
            println("뚜시뚜쉬")
            if (action == MotionEvent.ACTION_DOWN) {
                println("손가락 눌렸음 : $curX,$curY")
            } else if (action == MotionEvent.ACTION_MOVE) {
                println("손가락 움직임 : $curX,$curY")
            } else if (action == MotionEvent.ACTION_UP) {
                println("손가락 떼졌음 : $curX,$curY")
                var d = mediaController.lyrics.get(lrcView.indicatePosition).text
                println("여기는 " + d)
            }
            true
        })
    }

}

