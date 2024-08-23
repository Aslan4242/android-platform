package com.example.androidplatform.ui.dashboard

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentDashboardBinding
import com.example.androidplatform.domain.models.account.Account
import com.example.androidplatform.domain.models.cards.Card
import com.example.androidplatform.presentation.dashboard.adapter.StoriesAdapter
import com.example.androidplatform.presentation.dashboard.models.ScreenStateAccounts
import com.example.androidplatform.presentation.dashboard.models.ScreenStateCards
import com.example.androidplatform.presentation.dashboard.models.StoriesListState
import com.example.androidplatform.presentation.dashboard.viewmodel.DashBoardViewModel
import com.example.androidplatform.ui.stories.StoriesActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<DashBoardViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder
    private var cardsListData: List<Card> = emptyList()
    private var accountsListData: List<Account> = emptyList()
    lateinit var storiesAdapter: StoriesAdapter
    private var listData: List<Card> = emptyList()
    private var unviewedStories: List<Int> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCards()
        viewModel.getAccounts()
        unviewedStories = viewModel.getUnviewedStories()

        storiesAdapter = StoriesAdapter { storyPosition, storiesCount ->
            val intent = Intent(requireContext(), StoriesActivity::class.java)
                .putExtra("storyPosition", storyPosition)
                .putExtra("storiesCount", storiesCount)
            startActivity(intent)
        }
        binding.rvStories.adapter = storiesAdapter
        setItemDecoration()
        viewModel.getStories()

        val swipeRefreshLayout = binding.dashboardSrl
        swipeRefreshLayout.setOnRefreshListener {
            viewModel.getCards()
            viewModel.getAccounts()
            swipeRefreshLayout.isRefreshing = false
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        viewModel.cardsScreenState().observe(viewLifecycleOwner) {
            renderCards(it)
        }

        viewModel.accountsScreenState().observe(viewLifecycleOwner) {
            renderAccounts(it)
        }

        binding.orderCardBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_cardsFragment)
        }

        binding.openAccountBtn.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_accountsFragment)
        }

        viewModel.storiesListState().observe(viewLifecycleOwner) {
            render(it)
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_exit)
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Выйти") { dialog, which ->
                findNavController().navigate(R.id.action_pop_back)
            }
    }

    private fun renderCards(state: ScreenStateCards) {
        when (state) {
            is ScreenStateCards.Content -> {
                cardsListData = state.cards
                val expandableListView = binding.cardsNlv
                val adapter = CardsExpandableListAdapter(
                    context = requireContext(),
                    listGroupTitle = resources.getString(R.string.cards),
                    listItems = cardsListData,
                    onCardClickListener = { cardId ->
                        findNavController().navigate(DashboardFragmentDirections
                            .actionDashboardFragmentToCardInfoFragment(cardId))
                    }
                )
                expandableListView.setAdapter(adapter)
            }

            else -> {}
        }
    }

    private fun renderAccounts(state: ScreenStateAccounts) {
        when (state) {
            is ScreenStateAccounts.Content -> {
                accountsListData = state.accounts
                val expandableListView = binding.accountsNlv
                val adapter = AccountsExpandableListAdapter(
                    resources.getString(R.string.accounts),
                    accountsListData
                )
                expandableListView.setAdapter(adapter)
            }

            else -> {}
        }
    }

    private fun setItemDecoration() {
        binding.rvStories.addItemDecoration(object : ItemDecoration() {
            private var mBounds = Rect()
            private val borderMarginDp = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 7f, resources.displayMetrics)
            private val borderCornerRadius = TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics)
            private val borderPaint = Paint().apply {
                color = resources.getColor(R.color.orange)
                style = Paint.Style.STROKE
                strokeWidth = 5f
                isAntiAlias = true
            }

            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                super.onDraw(c, parent, state)
                for (i in 0 until parent.childCount) {
                    val child = parent.getChildAt(i)
                    if (parent.getChildAdapterPosition(child) !in unviewedStories) continue
                    parent.getDecoratedBoundsWithMargins(child, mBounds)
                    c.drawRoundRect(
                        mBounds.left + borderMarginDp,
                        mBounds.top + borderMarginDp,
                        mBounds.right - borderMarginDp,
                        mBounds.bottom - borderMarginDp,
                        borderCornerRadius,
                        borderCornerRadius,
                        borderPaint
                    )
                }
            }
        })
    }

    private fun render(state: StoriesListState) {
        when (state) {
            is StoriesListState.Content -> storiesAdapter.submitList(state.stories)
            else -> {}
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            confirmDialog.show()
        }
    }
}
