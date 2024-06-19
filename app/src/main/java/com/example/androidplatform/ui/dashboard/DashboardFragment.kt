package com.example.androidplatform.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentDashboardBinding
import com.example.androidplatform.presentation.dashboard.models.DashboardState
import com.example.androidplatform.presentation.dashboard.viewmodel.DashBoardViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardFragment : Fragment()  {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<DashBoardViewModel>()
    lateinit var confirmDialog: MaterialAlertDialogBuilder


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
        val window = requireActivity().window
        window.navigationBarColor = resources.getColor(R.color.gray_3)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        viewModel.screenState().observe(viewLifecycleOwner) {
            render(it)
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_exit)
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Выйти") { dialog, which ->
                findNavController().navigate(R.id.action_dashboardFragment_to_authorizationFragment)
            }
    }

    private fun render(state: DashboardState) {
        when (state) {
            is DashboardState.Content -> {
            }
            else -> {}
        }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            confirmDialog.show()
        }
    }
}