package com.hklee.musicplayer.network

import com.hklee.musicplayer.data.Song
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ApplicationComponent
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import javax.inject.Singleton
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject




class MainRepo @Inject constructor(private val apiService: ApiService) {
    fun getSong(): Flowable<Song> = apiService.getSong()
}

interface ApiService  {
    @GET("/2020-flo/song.json")
    fun getSong() : Flowable<Song>
}
