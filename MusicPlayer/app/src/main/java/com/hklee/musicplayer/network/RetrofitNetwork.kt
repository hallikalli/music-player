package com.hklee.musicplayer.network

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
import retrofit2.Call
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

@InstallIn(ActivityRetainedComponent::class)
@Module
object ApiModule {
    @Provides
    fun getApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)
}

@InstallIn(ApplicationComponent::class)
@Module
object RetrofitModule {
    @Singleton
    @Provides
    fun getRetrofit(client: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(client)
        .build()

    @Provides
    fun getClient() : OkHttpClient = OkHttpClient()
}

