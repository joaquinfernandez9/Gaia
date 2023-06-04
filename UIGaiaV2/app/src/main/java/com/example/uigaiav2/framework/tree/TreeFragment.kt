package com.example.uigaiav2.framework.tree

import android.content.Context.MODE_PRIVATE
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.uigaiav2.R
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream

@AndroidEntryPoint
class TreeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var currentUser: String
    private val viewModel: TreeViewModel by viewModels()
    private val cryptoManager = CryptoManager()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        FirebaseApp.initializeApp(requireContext())
        currentUser = cryptoManager.readTxt(requireContext())
        getTree()

        return binding.root
    }


    private fun getTree() {
        with(binding) {
            viewModel.handleEvent(TreeEvent.GetTree(currentUser))
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.state.collect { value ->
                        if (value.error != null) {
                            Snackbar.make(
                                binding.root,
                                "Can't load tree. Try again later.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            viewModel.handleEvent(TreeEvent.ClearError)
                        } else if (value.tree != null){
                            val tree = value.tree
                            val treeLvl = tree?.level //int to get img
                            treeImageView.visibility = View.GONE
                            progressBarImg.visibility = View.VISIBLE
                            //FirebaseApp.initializeApp(requireContext())
                            val storage = FirebaseStorage.getInstance()
                            val storageRef = storage.reference
                            val pathReference = storageRef.child("tree/$treeLvl.png")
                            val localFile = File.createTempFile("images", "png")
                            pathReference.getFile(localFile).addOnSuccessListener {
                                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                                treeImageView.setImageBitmap(bitmap)
                                treeImageView.visibility = View.VISIBLE
                                progressBarImg.visibility = View.GONE
                            }
                            // cargar barra progreso
                            tree?.progress?.let { progressBar.progress = it }
                            // cargar texto
                            val progress = getString(R.string.progress_text, tree?.progress)
                            progressText.text = progress
                        }
                    }
                }
            }

            waterButton.setOnClickListener {
                Snackbar.make(
                    binding.root,
                    "Watering tree...",
                    Snackbar.LENGTH_SHORT
                ).show()

                viewModel.handleEvent(TreeEvent.DeleteCompletedTasks(currentUser))
                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.state.collect { value ->
                            if (value.error != null) {
                                Snackbar.make(
                                    binding.root,
                                    "Can't load tree. Try again later.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.handleEvent(TreeEvent.ClearError)
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    "Tasks completed: ${value.completedTasks} since last time.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                                viewModel.handleEvent(TreeEvent.ClearCompletedTasks)
                            }
                        }
                    }
                }
            }
        }
    }




}