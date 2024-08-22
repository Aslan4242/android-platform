package com.example.androidplatform.presentation.dashboard.click_listeners

fun interface OnStoryClickListener {
    fun onClick(storyPosition: Int, storiesCount: Int): Unit
}
