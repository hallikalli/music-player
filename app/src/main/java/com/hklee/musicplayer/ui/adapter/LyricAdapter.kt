package com.hklee.musicplayer.ui.adapter

import android.graphics.Typeface
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hklee.musicplayer.R



data class Lyc(val time: Long, val line: String)

class LyricAdapter(
    private val dataSet: List<Lyc>,
    @StyleRes val lyricStyle: Int = R.style.LyricViewMini
) :
    RecyclerView.Adapter<LyricAdapter.LyricHolder>() {
    // 가사 구간 추적 기능 on일때 해당 구간으로 변
    interface OnItemClickListener {
        fun onSeekTo(time: Long)
    }

    // 가사 구간 추적 기능 off일때 구간터치시 현제 페이지 종료
    interface OnCloseListener {
        fun onFinish()
    }

    var centerPos: Int = -1
    var isItemClickable = false
//    private var dataSet: List<Lyc> = listOf()

    private var itemClickListener: OnItemClickListener? = null
    private var closeListener: OnCloseListener? = null
    private var textAligment =
        if (lyricStyle == R.style.LyricViewMini) TextView.TEXT_ALIGNMENT_CENTER else TextView.TEXT_ALIGNMENT_TEXT_START
    private var textBigger =
        lyricStyle != R.style.LyricViewMini

    var recyclerView: RecyclerView? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView!!)
        this.recyclerView = recyclerView
    }

    inner class LyricHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                if (!isItemClickable) {
                    closeListener?.apply { onFinish() }
                } else {
                    val pos = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        itemClickListener?.apply { onSeekTo(dataSet[pos].time) }
                    }
                }
            }

        }

        val textView = itemView.findViewById<TextView>(R.id.tvLyric).apply {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                setTextAppearance(this.context, lyricStyle);
            } else {
                setTextAppearance(lyricStyle)
            }
            textAlignment = textAligment
            if (textBigger) setPadding(0, 16, 0, 16)

        }!!
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.itemClickListener = listener
    }

    fun setOnCloseListener(listener: OnCloseListener) {
        this.closeListener = listener
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

            setTypeface(null, if (isCurrent) Typeface.BOLD else Typeface.NORMAL);
        }

        val originalPos = IntArray(2)
        holder.textView.getLocationInWindow(originalPos)
    }


    override fun getItemCount() = dataSet.size
    var currentLine = -1
    var prevLine = currentLine

    fun updateTime(mills: Long) {
        //첫 소절
        if (currentLine == -1) {
            if (dataSet[0].time <= mills) currentLine = 0
        }

        if (currentLine >= 0) { //시간순대로 가사 추적
            if (dataSet[currentLine].time <= mills + 500) {
                for (linePos in currentLine + 1 until dataSet.size) {
                    if (dataSet[linePos].time <= mills + 500) {
                        if (linePos == 0 && dataSet[0].time < mills) currentLine = -1
                        else currentLine = linePos
                    } else break
                }

            } else {  //시크바를 움직여 이전 시간대로 갔을력때 가사 추적
                if (dataSet[0].time > mills) {
                    currentLine = -1
                } else {
                    for (linePos in dataSet.indices) {
                        if (dataSet[linePos].time <= mills) {
                            currentLine = linePos
                        } else break
                    }
                }
            }
        }

        if (prevLine != currentLine) {
            if (currentLine >= 0) notifyItemChanged(currentLine)
            if (prevLine >= 0) notifyItemChanged(prevLine)
            //중앙고정이 설정되어있으면 자동 스크롤
            if (centerPos != -1 && centerPos < currentLine + 1) {


                Handler().postDelayed(
                    Runnable {
                        recyclerView!!.smoothScrollToPosition(currentLine + 1 - centerPos)

                    },
                    200
                )
//                recyclerView?.scrollToPosition(centerPos)
//                recyclerView?.invalidate()

            }
            prevLine = currentLine
        }


    }


}