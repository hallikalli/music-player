package com.hklee.musicplayer.ui.viewmodel

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.lifecycle.*
import com.hklee.musicplayer.base.BaseViewModel
import com.hklee.musicplayer.ui.adapter.Lyc
import com.hklee.musicplayer.ui.adapter.LyricAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit


//TODO: 시간나면 exoPlayer, MediaBrowserServiceCompat 사용해보기
class PlayerViewModel : BaseViewModel(), SeekBar.OnSeekBarChangeListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    LyricAdapter.OnItemClickListener {

    private val _playingProgress = MutableLiveData<Pair<Int, Int>>()
    val playingProgress = _playingProgress

    private val _lyrics = mutableListOf<Lyc>()
    val lyrics = _lyrics

    private val _isPlaying = MutableLiveData<Boolean>().apply {
        postValue(false)
    }
    val isPlaying = _isPlaying

    private var mediaPlayerObservable = Observable.interval(500L, TimeUnit.MILLISECONDS)
        .timeInterval()
        .observeOn(AndroidSchedulers.mainThread())

    private var songUrl: String? = null

    private var mediaPlayer = MediaPlayer().apply {
        setVolume(1f, 1f)
        setOnCompletionListener(this@PlayerViewModel)
        setOnErrorListener(this@PlayerViewModel)
        val attr = AudioAttributes.Builder().apply {
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            setUsage(AudioAttributes.USAGE_MEDIA)
        }.build()
        setAudioAttributes(attr)
    }

    fun setSongAndPrepare(url: String) {
        songUrl = url;
        mediaPlayer.reset()
        mediaPlayer.setDataSource(songUrl)
        mediaPlayer.prepare()
        if (compositeDisposable.size() == 0) {
            var disposable = mediaPlayerObservable.subscribe {
                _playingProgress.postValue(Pair(mediaPlayer.currentPosition, mediaPlayer.duration))
            }
            compositeDisposable.add(disposable)
        }
    }


    fun setLyrics(str: String) {
        var strs = str.split("\n")
        for (line in strs) {
            var timeStr = line.substring(line.indexOf('[') + 1, line.indexOf(']'))
            var lyric = line.substring(line.indexOf(']') + 1)
            _lyrics.add(Lyc(parseTime(timeStr), lyric))
        }
    }

    fun start() {
        if (songUrl == null) return
        mediaPlayer.start()
        _isPlaying.postValue(true);
    }

    fun pause(trackTouch: Boolean) {
        if (!mediaPlayer.isPlaying) return
        mediaPlayer.pause()
        _isPlaying.postValue(trackTouch);
    }

    fun stop() {
        mediaPlayer.pause()
        mediaPlayer.seekTo(0)
        _isPlaying.postValue(false);
        _playingProgress.postValue(Pair(0, mediaPlayer.duration))
    }

    override fun onCleared() {
        mediaPlayer.release()
        super.onCleared()
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) mediaPlayer.seekTo(seekBar.progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) = pause(true)

    override fun onStopTrackingTouch(seekBar: SeekBar) = start()

    override fun onCompletion(mp: MediaPlayer) = stop()

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        stop()
        return true;
    }

    override fun onSeekTo(time: Long) = mediaPlayer.seekTo(time.toInt())

    // ex [02:19:400]
    private fun parseTime(time: String): Long {
        val times = time.split(":")
        val min = times[0].toInt()
        val sec = times[1].toInt()
        val mil = times[2].toInt()
        return ((min * 60 + sec) * 1000 + mil).toLong()
    }

}