package com.example.uigaiav2.framework.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uigaiav2.R
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentFriendsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader

@AndroidEntryPoint
class FriendsFragment : Fragment() {

    private lateinit var _binding: FragmentFriendsBinding
    private val binding get() = _binding
    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var friendsAdapter: FriendsAdapter
    private val cryptoManager = CryptoManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        init()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    state.error?.let {
                        val msj: String = if (it.contains("400")) {
                            "Server error."
                        } else if (it.contains("404") || it.contains("204")) {
                            "No friends found."
                        } else {
                            "Request sent."
                        }
                        Snackbar.make(binding.root, msj, Snackbar.LENGTH_SHORT).show()
                        viewModel.handleEvent(FriendsEvent.ErrorShown)
//                        viewModel.handleEvent(FriendsEvent.GetFriends(cryptoManager.readTxt(requireContext())))
                    }
                    state.trees?.let {
                        friendsAdapter.submitList(it)
                    }
                }
            }
        }
        binding.recyclerView.adapter = friendsAdapter
        //swipe
        val touchHelper = ItemTouchHelper(friendsAdapter.swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)


        return binding.root
    }

    private fun init() {
        with(binding) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            val divider = MaterialDividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL /*or LinearLayoutManager.HORIZONTAL*/
            )
            recyclerView.addItemDecoration(divider)
            friendsAdapter = FriendsAdapter(requireContext(), object : FriendsAdapter.Actions {
                override fun onClickDetele(friend: String) {
                    viewModel.handleEvent(
                        FriendsEvent.DeclineRequest(
                            cryptoManager.readTxt(
                                requireContext()
                            ), friend
                        )
                    )
                }
            })
            viewModel.handleEvent(FriendsEvent.GetFriends(cryptoManager.readTxt(requireContext())))
            recyclerView.adapter = friendsAdapter

            floatingActionGetPetitions.setOnClickListener {
                //navigate to petitions
                findNavController().navigate(R.id.action_navigation_friends_to_navigation_requests)
            }

            floatingActionButton.setOnClickListener {
                setUpDialog()
            }
        }
    }

    private fun setUpDialog() {
        val inputLayout = TextInputLayout(requireContext())
        val inputEditText = EditText(requireContext())


        inputLayout.addView(inputEditText)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add friend")
            .setMessage("Enter your friend's username")
            .setView(inputLayout)
            .setNeutralButton(resources.getString(R.string.cancel)) { _, _ ->
                Snackbar.make(
                    binding.root,
                    "Petition canceled",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            .setPositiveButton(resources.getString(R.string.accept)) { _, _ ->
                val text = inputEditText.text.toString()
                viewModel.handleEvent(
                    FriendsEvent.SendRequest(
                        cryptoManager.readTxt(requireContext()),
                        text
                    )
                )

            }
            .show()
    }


}