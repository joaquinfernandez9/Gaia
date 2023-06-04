package com.example.uigaiav2.framework.task

import android.app.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.uigaiav2.domain.usecases.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentAddTaskDialogBinding
import com.example.uigaiav2.databinding.FragmentTasksBinding
import com.example.uigaiav2.domain.model.dto.TaskDTO
import com.example.uigaiav2.domain.usecases.notification.AlarmItem
import com.example.uigaiav2.domain.usecases.notification.AlarmScheduler
import com.example.uigaiav2.domain.usecases.notification.AndroidAlarmScheduler
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.time.LocalDateTime
import java.util.*

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private lateinit var _binding: FragmentTasksBinding
    private val binding get() = _binding
    private lateinit var _bindingDialog: FragmentAddTaskDialogBinding
    private val bindingDialog get() = _bindingDialog
    private lateinit var taskAdapter: TaskAdapter
    private val cryptoManager = CryptoManager()
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var alarmScheduler: AlarmScheduler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val divider = MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL /*or LinearLayoutManager.HORIZONTAL*/
        )
        binding.recyclerView.addItemDecoration(divider)
        init()
        viewModel.handleEvent(TaskEvent.GetTasks(cryptoManager.readTxt(requireContext())))
        binding.recyclerView.adapter = taskAdapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { value ->
                    value.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.handleEvent(TaskEvent.ErrorShown)
                    }
                    value.taskList.let {
                        taskAdapter.submitList(it?.map { task ->
                            task.toTaskDTO()
                        })
                    }
                }
            }
        }
        binding.recyclerView.adapter = taskAdapter
        //swipe
        val touchHelper = ItemTouchHelper(taskAdapter.swipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)
        setupFloatingActionButton()
        return binding.root
    }

    private fun init() {
        taskAdapter = TaskAdapter(context = requireContext(),
            object : TaskAdapter.Actions {
                override fun onclickUpdate(task: TaskDTO) {
                    viewModel.handleEvent(TaskEvent.UpdateTask(task))
                    viewModel.handleEvent(TaskEvent.GetTasks(cryptoManager.readTxt(requireContext())))
                }

                override fun onClickDelete(task: TaskDTO) {
                    viewModel.handleEvent(TaskEvent.DeleteTask(task))
                    viewModel.handleEvent(TaskEvent.GetTasks(cryptoManager.readTxt(requireContext())))
                }
            })
        alarmScheduler = AndroidAlarmScheduler(requireContext())
    }

    private fun setupFloatingActionButton() {
        binding.floatingActionButton.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialog = Dialog(requireContext())
        val bindingDialog = FragmentAddTaskDialogBinding.inflate(layoutInflater)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setLayout(1400, ViewGroup.LayoutParams.WRAP_CONTENT)

        var selectedEndTime: LocalDateTime? = null

        with(bindingDialog) {
            save.setOnClickListener {
                if (!titleET.text.isNullOrBlank()) {
                    if (selectedEndTime != null) {
                        val task = TaskDTO(
                            name = titleET.text.toString(),
                            initTime = LocalDateTime.now(),
                            endTime = selectedEndTime!!,
                            username = cryptoManager.readTxt(requireContext())
                        )
                        viewModel.handleEvent(TaskEvent.AddTask(task))
                        viewModel.handleEvent(TaskEvent.GetTasks(cryptoManager.readTxt(requireContext())))
                        viewLifecycleOwner.lifecycleScope.launch {
                            repeatOnLifecycle(Lifecycle.State.STARTED) {
                                viewModel.state.collect { value ->
                                    value.error?.let {
                                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                                            .show()
                                        viewModel.handleEvent(TaskEvent.ErrorShown)
                                    } ?: run {
                                        value.taskList?.let { taskList ->
                                            taskAdapter.submitList(taskList.map { task ->
                                                task.toTaskDTO()
                                            })
                                        }
                                    }
                                }
                            }
                        }
                        dialog.dismiss()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Select a valid date and time",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Task name cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }

            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                val calendar = Calendar.getInstance()
                calendar.set(
                    datePicker.year,
                    datePicker.month,
                    datePicker.dayOfMonth,
                    hourOfDay,
                    minute
                )
                selectedEndTime =
                    LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId())
            }
            dialog.setOnDismissListener {
                if (selectedEndTime != null) {
                    val alarmItem = AlarmItem(selectedEndTime!!, titleET.text.toString())
                    alarmScheduler.schedule(alarmItem)
                }
            }
            dialog.show()
        }

    }





}
