package com.example.androidplatform.ui.stories

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import com.example.androidplatform.R
import com.example.androidplatform.databinding.ActivityStoriesBinding

class StoriesActivity : FragmentActivity() {

    private val binding by lazy { ActivityStoriesBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .add(
                R.id.fragment_container,
                StoriesFragment::class.java,
                bundleOf(
                    "storiesCount" to intent.getIntExtra("storiesCount", 10),
                    "storyPosition" to intent.getIntExtra("storyPosition", 0)
                )
            )
            .commit()
    }
}