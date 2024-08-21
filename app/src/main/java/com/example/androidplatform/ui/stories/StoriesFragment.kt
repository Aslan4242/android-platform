package com.example.androidplatform.ui.stories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.MotionLayout.TransitionListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.androidplatform.databinding.FragmentStoriesBinding

class StoriesFragment : Fragment() {
    private var _binding: FragmentStoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StoryViewPagerAdapter
    private lateinit var onPageScrollStateChangeCallback: () -> Unit

    private val TAG = "TAG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = StoryViewPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle,
            itemCount = requireArguments().getInt("storiesCount")
        )
        binding.vpStories.adapter = adapter
        binding.vpStories.setCurrentItem(requireArguments().getInt("storyPosition"), false)
        binding.vpStories.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // nothing
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // nothing
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    onPageScrollStateChangeCallback.invoke()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun switchStory(diff: Int) = with(binding.vpStories) {
        setCurrentItem(currentItem + diff)
    }

    fun setUpPageSelectedCallback(callback: () -> Unit) {
        onPageScrollStateChangeCallback = callback
    }
}
