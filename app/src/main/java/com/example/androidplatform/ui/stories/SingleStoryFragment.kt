package com.example.androidplatform.ui.stories

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.databinding.FragmentSingleStoryBinding
import com.example.androidplatform.domain.models.stories.Story
import com.example.androidplatform.presentation.stories.models.SingleStoryScreenState
import com.example.androidplatform.presentation.stories.viewmodel.SingleStoryViewModel
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrInterface
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import com.r0adkll.slidr.util.ViewDragHelper
import org.koin.androidx.viewmodel.ext.android.viewModel

class SingleStoryFragment : Fragment() {
    private var _binding: FragmentSingleStoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SingleStoryViewModel>()
    private var slidrInterface: SlidrInterface? = null

    private val TAG = "TAG"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getStoryById(arguments?.getInt("page")!!)
        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.setStoryViewed(arguments?.getInt("page")!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: SingleStoryScreenState) {
        when (state) {
            is SingleStoryScreenState.Content -> showContent(state.story)
            else -> {}
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showContent(story: Story) {
        var touchEventTime = 0L
        with(binding) {
            tvDescription.text = story.text
            ivBackground.setImageResource(story.image)
            pbStory.setOnProgressEndListener {
                if (isAdded) {
                    (requireParentFragment() as? StoriesFragment)?.switchStory(1)
                }
            }
            ivBackground.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    touchEventTime = event.eventTime
                    binding.pbStory.pause()
                } else if (event.action == MotionEvent.ACTION_UP) {
                    if (event.eventTime - touchEventTime < 200L) {
                        if (event.x > binding.ivBackground.width / 2) {
                            if (isAdded) {
                                (parentFragment as? StoriesFragment)?.switchStory(1)
                            }
                        } else {
                            if (isAdded) {
                                (parentFragment as? StoriesFragment)?.switchStory(-1)
                            }
                        }
                    } else {
                        binding.pbStory.resume()
                    }
                } else if (event.action == MotionEvent.ACTION_CANCEL) {
                    binding.pbStory.cancel()
                }
                true
            }
            ivClose.setOnClickListener { findNavController().navigateUp() }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.pbStory.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (slidrInterface == null) slidrInterface = Slidr.replace(
            binding.contentContainer,
            SlidrConfig.Builder()
                .listener(object : SlidrListener {
                    override fun onSlideStateChanged(state: Int) {
                        if (state == ViewDragHelper.STATE_DRAGGING) {
                            if (isAdded) {
                                (parentFragment as? StoriesFragment)?.disablePagerSwipe()
                            }
                        } else if (state == ViewDragHelper.STATE_IDLE) {
                            if (isAdded) {
                                (parentFragment as? StoriesFragment)?.enablePagerSwipe()
                            }
                        }
                    }

                    override fun onSlideChange(percent: Float) {
                        // nothing
                    }

                    override fun onSlideOpened() {
                        binding.pbStory.resume()
                    }

                    override fun onSlideClosed(): Boolean {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                        return true
                    }
                })
                .position(SlidrPosition.TOP).build()
        )
        (requireParentFragment() as StoriesFragment).setUpPageSelectedCallback {
            binding.pbStory.start()
        }
        binding.pbStory.start()
    }

    companion object {
        fun newInstance(page: Int): SingleStoryFragment {
            val fragment = SingleStoryFragment()
            val arguments = Bundle().apply {
                putInt("page", page)
            }
            fragment.arguments = arguments
            return fragment
        }
    }
}
