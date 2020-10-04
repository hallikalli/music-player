package com.hklee.musicplayer.network

import com.hklee.musicplayer.data.Song
import io.reactivex.rxjava3.core.Flowable
import retrofit2.http.GET
import javax.inject.Inject


class MainRepo @Inject constructor(private val apiService: ApiService) {
    fun getSong(): Flowable<Song> = apiService.getSong()
}

interface ApiService  {
    @GET("/2020-flo/song.json")
    fun getSong() : Flowable<Song>
}
