package com.example.faith.ui.home

import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.faith.database.post.DatabasePost
import com.example.faith.domain.Post

@BindingAdapter("postImage")
fun ImageView.setPostImage(image: Bitmap?) {
    setImageBitmap(image)
}