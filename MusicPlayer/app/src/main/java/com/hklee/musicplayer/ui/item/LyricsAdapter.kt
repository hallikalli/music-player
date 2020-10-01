package com.hklee.musicplayer.ui.item

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.hklee.musicplayer.R

// RecyclerView.Adapter클래스를 상속받는 Custom Adapter클래스를 새로 만들자.
// onBindViewHolder에서 사용할 inner class인 Custom ViewHolder 를 인자로 갖는다.


class LyricsAdapter : RecyclerView.Adapter<LyricsAdapter.LyricsHolder>() {
    private lateinit var lyricsSet: List<Pair<Int, String>> // 시간(millsecond), 가

    // 커스텀 ViewHolder class를 생성하자.
    class LyricsHolder(v: TextView) : ViewHolder(v) {
        var textView: TextView = v.findViewById(R.id.lyrics) as TextView
    }

    fun LyricsAdapter(lyricsSet: List<Pair<Int, String>>) {
        this.lyricsSet = lyricsSet
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LyricsHolder {
        val v: TextView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_lyrics, parent, false) as TextView
        return LyricsHolder(v)
    }

    override fun onBindViewHolder(holder: LyricsHolder, position: Int) {
        holder.textView.setText(lyricsSet[position].second)
    }

    override fun getItemCount(): Int {
        return lyricsSet.size
    }


}