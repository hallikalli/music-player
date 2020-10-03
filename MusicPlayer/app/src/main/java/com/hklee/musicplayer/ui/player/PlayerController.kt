package com.hklee.musicplayer.ui.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.lauzy.freedom.library.Lrc
import com.lauzy.freedom.library.LrcView
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


enum class MUSIC_STATUS {
    PLAYING, STOP, PAUSE
}

interface PlayerController : SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    LrcView.OnPlayIndicatorLineListener,
    OnItemClickListener{
    val isPlaying: LiveData<Boolean> // todo: musicStatus와 통합
    val nowPlaying: LiveData<Pair<Int, Int>>
    val musicStatus: LiveData<MUSIC_STATUS>
    val lyrics: List<Lrc>
    val lyrics2: List<Lyc>
    fun setNewTrack(url: String)
    fun setLyrics(lyrics: String)
    fun start()
    fun pause(tracking: Boolean = false)
    fun stop()
}

//todo : application 종료시 mediaplayer.release
class PlayerControllerImpl : PlayerController {

    //todo: lazy 추가
    private var mediaPlayer = MediaPlayer().apply {
        setVolume(1f, 1f)
        setOnPreparedListener(this@PlayerControllerImpl)
        setOnCompletionListener(this@PlayerControllerImpl)
        setOnErrorListener(this@PlayerControllerImpl)
        val attr = AudioAttributes.Builder().apply {
            setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            setUsage(AudioAttributes.USAGE_MEDIA)
        }.build()
        setAudioAttributes(attr)
    }
    private val _nowPlaying = MutableLiveData<Pair<Int, Int>>()
    override val nowPlaying = _nowPlaying

    private val _musicStatus = MutableLiveData<MUSIC_STATUS>()
    override val musicStatus = _musicStatus.apply { postValue(MUSIC_STATUS.STOP) }

    private val _lyrics = mutableListOf<Lrc>()
    override val lyrics = _lyrics
    private val _lyrics2 = mutableListOf<Lyc>()
    override val lyrics2 = _lyrics2

    private var status = MUSIC_STATUS.STOP;

    private val _isPlaying = MutableLiveData<Boolean>().apply {
        postValue(false)
    }
    override val isPlaying = _isPlaying
    private val compositeDisposable = CompositeDisposable()

    var observable = Observable.interval(500L, TimeUnit.MILLISECONDS)
        .timeInterval()
        .observeOn(AndroidSchedulers.mainThread())
        .filter { MUSIC_STATUS.PLAYING == musicStatus.value }

    var songUrl: String? = null
    override fun setNewTrack(url: String) {
        if (MUSIC_STATUS.STOP != _musicStatus) {
            stop()
            mediaPlayer.reset()
        }
        songUrl = url;
    }

    override fun setLyrics(str: String) {
        var strs = str.split("\n")
        println("strsize " + strs.size)
        for (line in strs) {
            var timeStr = line.substring(line.indexOf('[') + 1, line.indexOf(']'))
            var lyric = line.substring(line.indexOf(']') + 1)
            println(timeStr + ". " + lyric)
            _lyrics.add(Lrc().apply {
                time = parseTime(timeStr)
                text = lyric
            })
            _lyrics2.add(Lyc(parseTime(timeStr),lyric))
        }
    }

    // ex [02:19:400]
    private fun parseTime(time: String): Long {
        val times = time.split(":")
        val min = times[0].toInt()
        val sec = times[1].toInt()
        val mil = times[2].toInt()
        return ((min * 60 + sec) * 1000 + mil).toLong()
    }

    override fun start() {
        if (songUrl == null) return
        if (MUSIC_STATUS.PAUSE == musicStatus.value) {
            mediaPlayer.start()
        }else {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(songUrl)
            mediaPlayer.prepareAsync()
        }
        _isPlaying.postValue(true);
        _musicStatus.postValue(MUSIC_STATUS.PLAYING)

    }

    //todo tracktouch 시 버튼이 바뀌면 안
    override fun pause(trackTouch: Boolean) {
        mediaPlayer.pause()
        _isPlaying.postValue(trackTouch);
        _musicStatus.postValue(MUSIC_STATUS.PAUSE)
    }

    override fun stop() {
        if (MUSIC_STATUS.STOP == musicStatus) return
        mediaPlayer.stop()
        _isPlaying.postValue(false);
        compositeDisposable.clear()
        _nowPlaying.postValue(Pair(0, mediaPlayer.duration))
        _musicStatus.value = MUSIC_STATUS.STOP

    }

    private fun playAndObserve() {
        mediaPlayer.start()
        _musicStatus.value = MUSIC_STATUS.PLAYING
        var disposable = observable.subscribe {
            _nowPlaying.postValue(Pair(mediaPlayer.currentPosition, mediaPlayer.duration))
        }
        compositeDisposable.add(disposable)
    }


    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (fromUser) mediaPlayer.seekTo(seekBar.progress)
    }


    override fun onStartTrackingTouch(seekBar: SeekBar) = pause(true)


    override fun onStopTrackingTouch(seekBar: SeekBar) = start()


    override fun onPrepared(mp: MediaPlayer) = playAndObserve()


    override fun onCompletion(mp: MediaPlayer) {
        stop()
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        stop()
        return true;
    }

    override fun onPlay(time: Long, content: String?) {
        mediaPlayer.seekTo(time.toInt())
    }

    override fun onSeekTo(time: Long) {
        mediaPlayer.seekTo(time.toInt())
    }

    /* override fun onBind(p0: Intent?): IBinder? {
       return null
     }*/


}