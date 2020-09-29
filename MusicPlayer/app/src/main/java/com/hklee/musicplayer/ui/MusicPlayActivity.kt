package com.hklee.musicplayer.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseActivity
import com.hklee.musicplayer.databinding.ActivityMusicPlayBinding
import com.hklee.musicplayer.network.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlayActivity : BaseActivity<ActivityMusicPlayBinding>(
    R.layout.activity_music_play
) {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.viewModel = mainViewModel

//        mainViewModel.mainRepo.getSong().subscribe { println(it) }
//        mainViewModel.
/*

        val retrofit = Retrofit.Builder()
            .baseUrl("https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(RetrofitNetwork::class.java)

        service.song().enqueue(object: Callback<Song> {
            override fun onFailure(call: Call<Song>, t: Throwable) {
                // 요청에 실패할 경우
                t.message?.let {
                    Toast.makeText(this@MusicPlayActivity, it, Toast.LENGTH_SHORT).show()
                } ?: Toast.makeText(this@MusicPlayActivity, "Unknown error", Toast.LENGTH_SHORT).show()

            }
            override fun onResponse(call: Call<Song>, response: Response<Song>) {
                // 요청에 성공한 경우
                response.body()?.let {
                    val album = it.album
                    val duration = it.duration
                    val file = it.file
                    val image = it.image
                    val lyrics = it.lyrics
                    val singer = it.singer
                    val title = it.title
                    sb_song_progeress_bar.max=Integer.valueOf(it.duration)
                    textView.text = "userId: $album\nid: $duration\ntitle: $lyrics\nbody: $title"
                } ?: Toast.makeText(this@MusicPlayActivity, "Body is null", Toast.LENGTH_SHORT).show()
            }
        })

*/


    }
}