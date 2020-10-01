package com.hklee.musicplayer.di

import com.hklee.musicplayer.ui.player.PlayerController
import com.hklee.musicplayer.ui.player.PlayerControllerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PlayerControllerModule {
    @Provides
    @Singleton
    fun providePlayerController(): PlayerController = PlayerControllerImpl()
}
