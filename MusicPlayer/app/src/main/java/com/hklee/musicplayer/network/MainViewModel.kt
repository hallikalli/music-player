package com.hklee.musicplayer.network

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainViewModel : ViewModel {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    var music = MutableLiveData<Music>()


    @ViewModelInject
    constructor(mainRepo: MainRepo) {
        var dis = mainRepo.getSong()
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe() {
                this.music.postValue(it)
                println("thit ${it.duration}, ${it.current}")
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