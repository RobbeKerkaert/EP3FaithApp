package com.example.faith.ui.post_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.faith.databinding.ListItemReactionBinding
import com.example.faith.domain.Reaction

class PostDetailAdapter(val clickListener: ReactionListener) : ListAdapter<Reaction, PostDetailAdapter.ViewHolder>(ReactionDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemReactionBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(clickListener: ReactionListener, item: Reaction) {
            binding.reaction = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemReactionBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class ReactionDiffCallback : DiffUtil.ItemCallback<Reaction>() {
    override fun areItemsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
        return oldItem.reactionId == newItem.reactionId
    }

    override fun areContentsTheSame(oldItem: Reaction, newItem: Reaction): Boolean {
        return oldItem == newItem
    }

}

class ReactionListener(val clickListener: (reactionId: Long, operation: Int) -> Unit) {
    fun onClickUpdate(reaction: Reaction) = clickListener(reaction.reactionId, 1)
    fun onClickDelete(reaction: Reaction) = clickListener(reaction.reactionId, 2)
}