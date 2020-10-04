package com.hklee.musicplayer.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hklee.musicplayer.base.BaseViewModel
import com.hklee.musicplayer.data.Song
import com.hklee.musicplayer.network.MainRepo
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class RepoModel @ViewModelInject constructor(mainRepo: MainRepo) : BaseViewModel() {
    private val _currentSong = MutableLiveData<Song>()
    val currentSong: LiveData<Song> = _currentSong
    init {
        var dis = mainRepo.getSong()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _currentSong.postValue(it)
            }
        compositeDisposable.add(dis)
    }
}