package com.hklee.musicplayer.ui.player

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.widget.SeekBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


enum class PLAYER_STATUS {
    INIT, PREPARE, PREPARED, PLAYING, PAUSE, STOP
}

interface PlayerController : SeekBar.OnSeekBarChangeListener, MediaPlayer.OnPreparedListener,
    MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
    OnItemClickListener{
    val isPlaying: LiveData<Boolean> // todo: musicStatus와 통합
    val nowPlaying: LiveData<Pair<Int, Int>>
    val playerStatus: LiveData<PLAYER_STATUS>
    val lyrics: List<Lyc>
    fun setNewSong(url: String)
    fun setLyrics(lyrics: String)
    fun start()
    fun pause(tracking: Boolean = false)
    fun stop()
}

//todo : application 종료시 mediaplayer.release
class PlayerControllerImpl : PlayerController {

    init {
//        playerStatus.observe(this, status -> {
//            when(status){
//
//            }
//        })
    }



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

    private val _playerStatus = MutableLiveData<PLAYER_STATUS>()
    override val playerStatus = _playerStatus.apply { postValue(PLAYER_STATUS.STOP) }


    private val _lyrics = mutableListOf<Lyc>()
    override val lyrics = _lyrics

    private var status = PLAYER_STATUS.STOP;

    private val _isPlaying = MutableLiveData<Boolean>().apply {
        postValue(false)
    }
    override val isPlaying = _isPlaying
    private val compositeDisposable = CompositeDisposable()

    var observable = Observable.interval(500L, TimeUnit.MILLISECONDS)
        .timeInterval()
        .observeOn(AndroidSchedulers.mainThread())
        .filter { PLAYER_STATUS.PLAYING == playerStatus.value }

    var songUrl: String? = null
    override fun setNewSong(url: String) {
        if (PLAYER_STATUS.STOP != _playerStatus) {
            stop()
            mediaPlayer.reset()
        }
        songUrl = url;
//        mediaPlayer.setDataSource(songUrl)
//        mediaPlayer.prepareAsync()
//        _playerStatus.value = PLAYER_STATUS.PREPARE
    }

    override fun setLyrics(str: String) {
        var strs = str.split("\n")
        for (line in strs) {
            var timeStr = line.substring(line.indexOf('[') + 1, line.indexOf(']'))
            var lyric = line.substring(line.indexOf(']') + 1)

            _lyrics.add(Lyc(parseTime(timeStr),lyric))
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

        mediaPlayer.start()

       if (PLAYER_STATUS.PAUSE == _playerStatus) {
            mediaPlayer.start()
        }else {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(songUrl)
            mediaPlayer.prepareAsync()
        }
        _isPlaying.postValue(true);
        _playerStatus.postValue(PLAYER_STATUS.PLAYING)

    }

    //todo tracktouch 시 버튼이 바뀌면 안
    override fun pause(trackTouch: Boolean) {
        mediaPlayer.pause()
        _isPlaying.postValue(trackTouch);
        _playerStatus.postValue(PLAYER_STATUS.PAUSE)
    }

    override fun stop() {
        if (PLAYER_STATUS.STOP == playerStatus) return
        mediaPlayer.stop()
        _isPlaying.postValue(false);
        compositeDisposable.clear()
        _nowPlaying.postValue(Pair(0, mediaPlayer.duration))
        _playerStatus.value = PLAYER_STATUS.STOP

    }

    private fun playAndObserve() {
        mediaPlayer.start()
        _playerStatus.value = PLAYER_STATUS.PLAYING
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


    override fun onPrepared(mp: MediaPlayer)  {
        playerStatus.value=PLAYER_STATUS.PREPARED
        playAndObserve()
    }


    override fun onCompletion(mp: MediaPlayer) {
        stop()
    }

    override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
        stop()
        return true;
    }


    override fun onSeekTo(time: Long) {
        mediaPlayer.seekTo(time.toInt())
    }

    /* override fun onBind(p0: Intent?): IBinder? {
       return null
     }*/


}