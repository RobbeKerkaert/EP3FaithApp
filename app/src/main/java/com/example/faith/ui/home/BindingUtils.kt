package com.example.faith.ui.home

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.faith.database.post.DatabasePost

@BindingAdapter("PostId")
fun TextView.setPostIdFormatted(item: DatabasePost?) {
    item?.let {
        text = item.postId.toString()
    }
}

@BindingAdapter("PostText")
fun TextView.setPostTextFormatted(item: DatabasePost?) {
    item?.let {
        text = item.text
    }
}