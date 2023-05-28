package com.example.uigaiav2.framework.task

import android.app.Dialog
import android.app.TimePickerDialog
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
import com.example.uigaiav2.R
import com.example.uigaiav2.crypt.CryptoManager
import com.example.uigaiav2.databinding.FragmentAddTaskDialogBinding
import com.example.uigaiav2.databinding.FragmentTasksBinding
import com.example.uigaiav2.domain.model.dto.TaskDTO
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@AndroidEntryPoint
class TaskFragment : Fragment() {
    private lateinit var _binding: FragmentTasksBinding
    private val binding get() = _binding
    private lateinit var taskAdapter: TaskAdapter
    private val cryptoManager = CryptoManager()
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        val divider = MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL /*or LinearLayoutManager.HORIZONTAL*/)
        binding.recyclerView.addItemDecoration(divider)
        init()
        viewModel.handleEvent(TaskEvent.GetTasks(readTxt()))
        binding.recyclerView.adapter = taskAdapter
        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.state.collect { value ->
                    value.error?.let {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                        viewModel.handleEvent(TaskEvent.ErrorShown)
                    }
                    value.taskList.let {
                        taskAdapter.submitList(it?.map { task->
                            task.toTaskDTO()
                        })
                    }
                }
            }
        }
        binding.recyclerView.adapter = taskAdapter
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
            }

            override fun onClickDelete(task: TaskDTO) {
                viewModel.handleEvent(TaskEvent.DeleteTask(task))
            }
        })
    }

    private fun setupFloatingActionButton() {
        binding.floatingActionButton.setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.fragment_add_task_dialog)
        dialog.window?.setLayout(700, ViewGroup.LayoutParams.WRAP_CONTENT)
        var selectedStartTime: LocalDateTime? = null
        var selectedEndTime: LocalDateTime? = null
        val bindingDialog = FragmentAddTaskDialogBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(bindingDialog.root) // Cambia esta línea para usar el bindingDialog

        with(bindingDialog) {
            startTimeButton.setOnClickListener {
                val startTimePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                    val currentDate = LocalDate.now()
                    selectedStartTime =
                        LocalDateTime.of(currentDate, LocalTime.of(hourOfDay, minute))
                }, 0, 0, false)
                startTimePicker.show()
            }
            endTimeButton.setOnClickListener {
                val endTimePicker = TimePickerDialog(requireContext(), { _, hourOfDay, minute ->
                    val currentDate = LocalDate.now()
                    selectedEndTime = LocalDateTime.of(currentDate, LocalTime.of(hourOfDay, minute))
                }, 0, 0, false)
                endTimePicker.show()
            }
            saveButton.setOnClickListener {
                if (selectedStartTime != null && selectedEndTime != null) {
                    // Ambas fechas y horas se han seleccionado correctamente
                    val task = TaskDTO(
                        name = readTxt(),
                        initTime = selectedStartTime!!,
                        endTime = selectedEndTime!!
                    )
                    // Aquí puedes realizar la lógica para guardar la tarea con las fechas y horas seleccionadas
                    addTask(task)
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Seleccione una fecha y hora válidas",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            cancelButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    fun addTask(task: TaskDTO) {

    }




    private fun readTxt(): String {
        var username = ""
        val file = File(requireContext().filesDir, "not_secret.txt")
        val secret = File(requireContext().filesDir, "secret.txt")
        if (file.exists()) {
            val bufferedReader = BufferedReader(FileReader(file))
            val stringBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line)
                line = bufferedReader.readLine()
            }
            val fileContent = stringBuilder.toString()
            bufferedReader.close()
            username = fileContent
        } else if (secret.exists()) {
            val fis = FileInputStream(secret)
            val bytes = cryptoManager.decrypt(inputStream = fis)
            val response = bytes.decodeToString()
            val list = response.split(" ")
            username = list[0]
        }
        return username
    }
}