package com.hklee.musicplayer.util

import android.content.Context
import com.hklee.musicplayer.R

object Utils {
    fun makeShortTimeString(context: Context, secs: Long): String {
        var seconds = secs
        val hours: Long
        val minutes: Long

        hours = seconds / 3600
        seconds %= 3600
        minutes = seconds / 60
        seconds %= 60

        val formatString = if (hours == 0L) {
            R.string.durationformatshort
        } else {
            R.string.durationformatlong
        }
        val durationFormat = context.resources.getString(formatString)
        return String.format(durationFormat, hours, minutes, seconds)
    }
}