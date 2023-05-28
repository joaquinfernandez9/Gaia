package com.example.uigaiav2.framework.task

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uigaiav2.R
import com.example.uigaiav2.databinding.ItemTaskBinding
import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.domain.model.dto.TaskDTO
import com.example.uigaiav2.framework.swipe.SwipeGesture
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TaskAdapter(val context: Context, private val actions: Actions) :
    ListAdapter<TaskDTO, TaskAdapter.TaskViewHolder>(TaskDiffCallback()){

    interface Actions {
        fun onClickDelete(task: TaskDTO)
        fun onclickUpdate(task: TaskDTO)
    }

    private var selectedItem = mutableListOf<TaskDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemTaskBinding.bind(view)
        fun bind(task: TaskDTO) = with(binding) {
            taskName.text = task.name
            taskDate.text = formatLocalDateTime(task.endTime)
        }

        private fun formatLocalDateTime(dateTime: LocalDateTime): String {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            return dateTime.format(formatter)
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<TaskDTO>() {
        override fun areItemsTheSame(oldItem: TaskDTO, newItem: TaskDTO): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: TaskDTO, newItem: TaskDTO): Boolean {
            return oldItem == newItem
        }
    }

    val swipeGesture = object : SwipeGesture(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when (direction) {
                ItemTouchHelper.RIGHT -> {
                    selectedItem.remove(getItem(viewHolder.adapterPosition))
                    actions.onClickDelete(getItem(viewHolder.adapterPosition))
                }
                ItemTouchHelper.LEFT -> {
                    actions.onclickUpdate(getItem(viewHolder.adapterPosition))
                }
            }
        }
    }

}