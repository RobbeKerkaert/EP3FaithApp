package com.example.faith.ui.home

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("postImage")
fun ImageView.setPostImage(image: Bitmap?) {
    setImageBitmap(image)
}
