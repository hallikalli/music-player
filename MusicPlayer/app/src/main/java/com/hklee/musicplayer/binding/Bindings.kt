package com.droidknights.app2020.binding

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.hklee.musicplayer.R
import com.hklee.musicplayer.util.Utils
import java.text.SimpleDateFormat


@BindingAdapter("imageUrl")
fun ImageView.bindAlbumImage(imageUrl: String?) {

    //todo: 라운드 코너
    Glide.with(this)
        .load(imageUrl)
        .centerCrop()
        .placeholder(R.color.darkGray)
        .transform(RoundedCorners(20),CenterCrop())
        .into(this)


}

var simpleDateFormat = SimpleDateFormat("mm:ss")

@BindingAdapter("time")
fun TextView.bindTime(seconds: Int?) {
    text = Utils.makeShortTimeString(context, (seconds ?: 0) / 1000.toLong())
}

@BindingAdapter("play")
fun ImageButton.play(play: Boolean?) {
    play?.let {
        @DrawableRes var res = if (it) R.drawable.ic_pause else R.drawable.ic_play
        this.setImageDrawable(ContextCompat.getDrawable(this.context, res))
    }
}




