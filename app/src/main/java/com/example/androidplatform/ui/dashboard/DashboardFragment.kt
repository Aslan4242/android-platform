package com.example.androidplatform.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidplatform.R
import com.example.androidplatform.databinding.FragmentAuthorizationBinding
import com.example.androidplatform.databinding.FragmentDashboardBinding
import com.example.androidplatform.databinding.FragmentRegiststrationBinding
import com.example.androidplatform.databinding.FragmentRestorePasswordBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DashboardFragment : Fragment()  {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
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

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            backPressedCallback
        )

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.do_you_want_exit)
            .setNeutralButton("Отмена") { dialog, which ->
                // ничего не делаем
            }.setNegativeButton("Выйти") { dialog, which ->
                findNavController().navigate(R.id.action_dashboardFragment_to_authorizationFragment)
            }
    }

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            confirmDialog.show()
        }
    }
}