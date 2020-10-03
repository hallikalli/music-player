package com.hklee.musicplayer.ui.player.lyricplayer

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.github.zagum.switchicon.SwitchIconView
import com.hklee.musicplayer.R
import com.hklee.musicplayer.base.BaseFragment
import com.hklee.musicplayer.data.Song
import com.hklee.musicplayer.databinding.FragmentLyricPlayerBinding
import com.hklee.musicplayer.ui.player.LyricAdapter
import com.hklee.musicplayer.ui.player.PlayerController
import com.hklee.musicplayer.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_lyric_player.*
import javax.inject.Inject


@AndroidEntryPoint
class LyricPlayerFragment :
    BaseFragment<FragmentLyricPlayerBinding, MainViewModel>(R.layout.fragment_lyric_player) {
    override val viewModel by activityViewModels<MainViewModel>()

    @Inject
    lateinit var playerController: PlayerController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true) //Make sure you have this line of code.
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actionbar_actions, menu);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_close -> {
                findNavController().navigateUp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    lateinit var adapter: LyricAdapter
    override fun init() {
        with(binding) {
            vm = viewModel
            mc = playerController
        }

        //ActionBar Setting
        activity?.let { it as AppCompatActivity }?.supportActionBar?.apply {
            viewModel.currentSong.observe(this@LyricPlayerFragment, Observer<Song> { song ->
                this.title = song.title
                this.subtitle = song.singer
            })
            setHomeAsUpIndicator(R.drawable.ic_clear)
            setDisplayShowHomeEnabled(true)
        }

//        rvLyric.adapter = LyricAdapter(R.style.LyricViewFull)
        adapter   = LyricAdapter(R.style.LyricViewFull)
        rvLyric.adapter = adapter
        adapter.textAligment = TextView.TEXT_ALIGNMENT_TEXT_START
        adapter.setData(playerController.lyrics)
        adapter.notifyDataSetChanged()
        rvLyric.layoutManager = LinearLayoutManager(activity);
        adapter.setOnItemClickListener(playerController)
        adapter.isItemClickable = binding.btnLyricClickable.isIconEnabled
        sbLyricPlayer.setOnSeekBarChangeListener(playerController)

        btnLyricClickable.setOnClickListener {
            var enable = (it as SwitchIconView).isIconEnabled

            adapter.isItemClickable = !enable
            it.setIconEnabled(!enable)
        }

        val playingObserver = Observer<Pair<Int, Int>> { pair ->
            (rvLyric.adapter as LyricAdapter).apply {
                updateTime(pair.first.toLong())

            }


        }
        playerController.nowPlaying.observe(this, playingObserver)

        btnExitLyricPlayer.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(rvLyric)
        val layoutManagaer = rvLyric.layoutManager
        val view: View? = snapHelper.findSnapView(layoutManagaer)

        adapter.centerPos = rvLyric.getChildAdapterPosition(view!!)
//        val itemView = rvLyric.getChildAdapterPosition( view)
        println(" iew = " + view + " . " + rvLyric.getChildAdapterPosition(view!!))

    }
}

