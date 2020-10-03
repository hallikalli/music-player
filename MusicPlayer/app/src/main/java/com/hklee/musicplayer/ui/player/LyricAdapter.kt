package com.hklee.musicplayer.ui.player

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hklee.musicplayer.R
import kotlinx.android.synthetic.main.fragment_lyric_player.*

interface OnItemClickListener {
    fun onSeekTo(time: Long)
}

data class Lyc(val time: Long, val line: String)

class LyricAdapter(@StyleRes val lyricStyle: Int = R.style.LyricViewMini) :
    RecyclerView.Adapter<LyricAdapter.LyricHolder>() {
    var centerPos: Int = -1
    private var mListener: OnItemClickListener? = null
    var dataSet: List<Lyc> = listOf()
    var isItemClickable = false
    var textAligment = TextView.TEXT_ALIGNMENT_CENTER

    var recyclerView: RecyclerView? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView!!)
        this.recyclerView = recyclerView
    }

    inner class LyricHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if (!isItemClickable) return@setOnClickListener
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    mListener?.apply { onSeekTo(dataSet[pos].time) }
                }
            }

        }

        //todo 수정하기
        val textView = itemView.findViewById<TextView>(R.id.tvLyric).apply {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                setTextAppearance(this.context, lyricStyle);
            } else {
                setTextAppearance(lyricStyle)
            }
            textAlignment = textAligment
        }!!
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LyricHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lyric_line, parent, false)
        view.findViewById<TextView>(R.id.tvLyric)
        return LyricHolder(view)
    }

    override fun onBindViewHolder(holder: LyricHolder, position: Int) {
        var isCurrent = (position == currentLine)
        holder.textView.apply {
            text = dataSet[position].line
            if (isCurrent)
                setTextColor(ContextCompat.getColor(context, R.color.lyricHighlight))
            else
                setTextColor(ContextCompat.getColor(context, R.color.lyricDefault))

            setTypeface(this.typeface, if (isCurrent) Typeface.BOLD else Typeface.NORMAL);
        }

        val originalPos = IntArray(2)
        holder.textView.getLocationInWindow(originalPos)
    }


    fun setData(dataSet: List<Lyc>) {
        this.dataSet = dataSet

    }


    override fun getItemCount() = dataSet.size
    var currentLine = -1
    var prevLine = currentLine

    fun updateTime(mills: Long) {
        if (currentLine == -1) {
            if (dataSet[0].time <= mills) currentLine = 0
        }
        if (currentLine >= 0) {
            if (dataSet[currentLine].time < mills) {
                for (linePos in currentLine + 1 until dataSet.size) {
                    if (dataSet[linePos].time <= mills) {
                        if (linePos == 0 && dataSet[0].time < mills) currentLine = -1
                        else currentLine = linePos
                    } else break
                }
            } else {
                for (linePos in dataSet.indices) {
                    if (dataSet[linePos].time <= mills) currentLine = linePos
                    else break
                }
            }
        }

        if (prevLine != currentLine) {
            if (prevLine >= 0) notifyItemChanged(prevLine)
            if (currentLine >= 0) notifyItemChanged(currentLine)
            prevLine = currentLine

            if (centerPos != -1 && centerPos < currentLine + 1) {
                println("$centerPos  $currentLine ")
                recyclerView?.scrollToPosition(currentLine + 1 - centerPos)
            }
        }


    }


}