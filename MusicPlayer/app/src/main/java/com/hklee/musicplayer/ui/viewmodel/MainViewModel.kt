package com.hklee.musicplayer.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hklee.musicplayer.base.BaseViewModel
import com.hklee.musicplayer.data.Song
import com.hklee.musicplayer.network.MainRepo
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel @ViewModelInject constructor(mainRepo: MainRepo) : BaseViewModel() {

    private val _playerStatus = MutableLiveData<Song>()
    val playerStatus: LiveData<Song> = _playerStatus

    private val _playProgress = MutableLiveData<Pair<Long, Long>>()
    val playProgress: LiveData<Pair<Long, Long>> = _playProgress

    init {
        var dis = mainRepo.getSong()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _playerStatus.postValue(it)
            }
        compositeDisposable.add(dis)
    }


}