package com.example.faith.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.faith.databinding.ListItemPostBinding
import com.example.faith.domain.Post

class HomeAdapter(val clickListener: PostListener) : ListAdapter<Post, HomeAdapter.ViewHolder>(PostDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemPostBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: PostListener, item: Post) {
            binding.post = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPostBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postId == newItem.postId
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}

class PostListener(val clickListener: (postId: Long, operation: Int) -> Unit) {
    fun onClick(post: Post) = clickListener(post.postId, 1)
    fun onClickDelete(post: Post) = clickListener(post.postId, 2)
    fun onClickUpdate(post: Post) = clickListener(post.postId, 3)
}