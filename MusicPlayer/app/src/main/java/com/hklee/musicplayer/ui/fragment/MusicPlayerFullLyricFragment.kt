package com.hklee.musicplayer.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.github.zagum.switchicon.SwitchIconView
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseFragment
import com.hklee.musicplayer.databinding.FragmentLyricPlayerBinding
import com.hklee.musicplayer.ui.adapter.LyricAdapter
import com.hklee.musicplayer.ui.viewmodel.PlayerViewModel
import com.hklee.musicplayer.ui.viewmodel.RepoModel
import kotlinx.android.synthetic.main.fragment_lyric_player.*


class MusicPlayerFullLyricFragment :
    BaseFragment<FragmentLyricPlayerBinding, RepoModel>(R.layout.fragment_lyric_player) {
    override val mainViewModel by activityViewModels<RepoModel>()
    private val playerViewModel by activityViewModels<PlayerViewModel>()
    private lateinit var preferences: SharedPreferences
    lateinit var adapter: LyricAdapter

    companion object {
        val PREFERENCE_LYRIC_CLICKABLE = "lyricClickable"
        val PREFERENCE_NAME = "app"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = activity?.getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)!!;

    }

    override fun init() {
        with(binding) {
            vm = mainViewModel
            player = playerViewModel
        }

        initPlayerBar()
        initLyricView()
        initLyricClickButton()
        initExitButton()
    }

    override fun onResume() {
        super.onResume()

        //RecyclerView에서 중간 지점의 포지션을 찾는 과정
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvLyric)
        val layoutManagaer = rvLyric.layoutManager
        val view: View? = snapHelper.findSnapView(layoutManagaer)
        adapter.centerPos = rvLyric.getChildAdapterPosition(view!!)
    }


    private fun finish() {
        findNavController().navigateUp()
    }


    private fun initPlayerBar() {
        sbLyricPlayer.setOnSeekBarChangeListener(playerViewModel)
    }

    private fun initLyricView() {
        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            (rvLyric.adapter as LyricAdapter).apply {
                updateTime(pair.first.toLong())
            }
        }

        adapter = LyricAdapter(playerViewModel.lyrics, R.style.LyricViewFull).apply {
            setOnItemClickListener(playerViewModel)
            isItemClickable = preferences.getBoolean(PREFERENCE_LYRIC_CLICKABLE, false)
            setOnCloseListener(object : LyricAdapter.OnCloseListener {
                override fun onFinish() {
                    finish()
                }
            })
        }
        rvLyric.apply {
            adapter = this@MusicPlayerFullLyricFragment.adapter
            layoutManager = LinearLayoutManager(activity);
        }
        playerViewModel.playingProgress.observe(this, playingObserver)
    }

    private fun initLyricClickButton() {
        btnLyricClickable.apply {
            setIconEnabled(preferences.getBoolean(PREFERENCE_LYRIC_CLICKABLE, false))
            setOnClickListener {
                var enable = (it as SwitchIconView).isIconEnabled
                adapter.isItemClickable = !enable
                it.setIconEnabled(!enable)
                preferences.edit().putBoolean(PREFERENCE_LYRIC_CLICKABLE, !enable).apply()
            }
        }
    }

    private fun initExitButton() {
        btnExitLyricPlayer.setOnClickListener {
            finish()
        }
    }
}

