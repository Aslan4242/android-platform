package com.example.androidplatform.presentation.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidplatform.databinding.ItemDashboardStoryBinding
import com.example.androidplatform.domain.models.stories.Story
import com.example.androidplatform.presentation.dashboard.adapter.StoriesAdapter.StoryViewHolder
import com.example.androidplatform.presentation.dashboard.click_listeners.OnStoryClickListener

class StoriesAdapter(
    private val onStoryClickListener: OnStoryClickListener
) : ListAdapter<Story, StoryViewHolder>(StoriesItemDiffCallback) {

    class StoryViewHolder(
        private val binding: ItemDashboardStoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            story: Story,
            storyPosition: Int,
            storiesCount: Int,
            clickListener: OnStoryClickListener
        ) {
            binding.storyLabel.text = story.label
            binding.storyDrawable.setImageResource(story.image)
            binding.storyCard.setOnClickListener { _ ->
                clickListener.onClick(storyPosition, storiesCount)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemDashboardStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        holder.bind(story, position, itemCount, onStoryClickListener)
    }

    object StoriesItemDiffCallback : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
            return oldItem == newItem
        }
    }
}
