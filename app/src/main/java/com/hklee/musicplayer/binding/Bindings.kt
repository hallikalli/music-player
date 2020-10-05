package com.hklee.musicplayer.binding

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.hklee.musicplayer.R
import com.hklee.musicplayer.util.Utils


@BindingAdapter("imageUrl")
fun ImageView.bindAlbumImage(imageUrl: String?) {
    val transformation = MultiTransformation(CenterCrop(), RoundedCorners(10))
    Glide.with(this)
        .load(imageUrl)
        .transform(transformation)
        .placeholder(R.color.darkGray)
        .into(this)
}

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




