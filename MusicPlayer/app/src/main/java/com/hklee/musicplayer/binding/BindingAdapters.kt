package com.droidknights.app2020.binding

import android.media.AudioManager
import android.media.MediaPlayer
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat


@BindingAdapter("imageUrl")
fun ImageView.bindAlbumImage(imageUrl: String?) {
    Glide.with(this)
        .load(imageUrl)
        .apply(RequestOptions.centerCropTransform())
        .into(this)
}

var simpleDateFormat = SimpleDateFormat("mm:ss")

@BindingAdapter("time")
fun TextView.bindTime(seconds: Int?) {
}



