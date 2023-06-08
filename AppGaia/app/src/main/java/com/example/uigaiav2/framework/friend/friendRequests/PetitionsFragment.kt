package com.example.uigaiav2.framework.friend.friendRequests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentPetitionsBinding
import com.example.uigaiav2.framework.friend.FriendsEvent
import com.example.uigaiav2.framework.friend.FriendsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader

@AndroidEntryPoint
class PetitionsFragment : Fragment() {
    private var _binding: FragmentPetitionsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FriendsViewModel by viewModels()
    private lateinit var petitionsAdapter: PetitionsAdapter
    private val cryptoManager = CryptoManager()
    private lateinit var currentUser: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPetitionsBinding.inflate(inflater, container, false)
        init()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                state.error?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    viewModel.handleEvent(FriendsEvent.ErrorShown)
                }
                state.friends?.let {
                    petitionsAdapter.submitList(it)
                }
            }
        }
        binding.petitionsRecycler.adapter = petitionsAdapter

        val touchHelper = ItemTouchHelper(petitionsAdapter.swipeGesture)
        touchHelper.attachToRecyclerView(binding.petitionsRecycler)

        return binding.root
    }


    private fun init() {
        currentUser = cryptoManager.readTxt(requireContext())
        with(binding) {
            petitionsRecycler.layoutManager = LinearLayoutManager(requireContext())
            val divider = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
            petitionsRecycler.addItemDecoration(divider)
            petitionsAdapter = PetitionsAdapter(requireContext(), object : PetitionsAdapter.Actions {
                override fun onClickAccept(friend: String) {
                    viewModel.handleEvent(FriendsEvent.AcceptRequest(currentUser, friend))
                }

                override fun onClickDecline(friend: String) {
                    viewModel.handleEvent(FriendsEvent.DeclineRequest(currentUser,friend))
                }
            })
            viewModel.handleEvent(FriendsEvent.GetPetitions(currentUser))
            petitionsRecycler.adapter = petitionsAdapter

        }
    }


}