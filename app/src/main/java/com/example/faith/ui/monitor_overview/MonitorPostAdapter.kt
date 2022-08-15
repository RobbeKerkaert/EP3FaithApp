package com.example.faith.ui.monitor_overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.faith.databinding.ListItemPostMonitorBinding
import com.example.faith.domain.Post

class MonitorPostAdapter(val clickListener: MonitorPostListener) : ListAdapter<Post, MonitorPostAdapter.ViewHolder>(MonitorPostDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemPostMonitorBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: MonitorPostListener, item: Post) {
            binding.post = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemPostMonitorBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class MonitorPostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.postId == newItem.postId
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}

class MonitorPostListener(val clickListener: (postId: Long, operation: Int) -> Unit) {
    fun onClick(post: Post) = clickListener(post.postId, 1)
}
