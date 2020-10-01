package com.hklee.musicplayer.network

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel @ViewModelInject constructor(mainRepo: MainRepo) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private val _songStatusLiveData = MutableLiveData<Song>()
    val songStatusLiveData: LiveData<Song> = _songStatusLiveData

    private val _nowPlayingLiveData = MutableLiveData<Pair<Long, Long>>()
    val nowPlayingLiveData: LiveData<Pair<Long, Long>> = _nowPlayingLiveData

/*
    private val castProgressListener =
        RemoteMediaClient.ProgressListener { progress, duration ->
            _nowPlayingLiveData.postValue(Pair(progress, duration))
        }
*/

    init {
        var dis = mainRepo.getSong()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _songStatusLiveData.postValue(it)
//                _nowPlayingLiveData.postValue(Pair(0, it.duration as Long))
            }
        addDisposable(dis)
    }

    private fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}/*class MainViewModel @ViewModelInject constructor(
    val mainRepo: MainRepo
) : ViewModel() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}*/