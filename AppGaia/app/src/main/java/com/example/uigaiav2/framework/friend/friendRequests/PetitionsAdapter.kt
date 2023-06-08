package com.example.uigaiav2.framework.friend.friendRequests

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uigaiav2.R.layout.item_petition
import com.example.uigaiav2.databinding.ItemPetitionBinding
import com.example.uigaiav2.domain.model.Friend
import com.example.uigaiav2.framework.swipe.SwipeGesture

class PetitionsAdapter(val context: Context, private val actions: Actions) :
    ListAdapter<Friend, PetitionsAdapter.PetitionsViewHolder>(PetitionsDiffCallback()) {


    interface Actions {
        fun onClickAccept(friend: String)
        fun onClickDecline(friend: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetitionsViewHolder {
        return PetitionsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(item_petition, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PetitionsViewHolder, position: Int) = with(holder) {
        val item = getItem(position)
        bind(item)
    }

    inner class PetitionsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemPetitionBinding.bind(view)
        fun bind(friend: Friend) = with(binding) {
            petitionName.text = friend.username2
        }
    }

    class PetitionsDiffCallback :
        DiffUtil.ItemCallback<Friend>() {
        override fun areItemsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            // si la peticion me ha llegado a mi significa que username2 es mi username
            return oldItem.username2 == newItem.username2
        }

        override fun areContentsTheSame(oldItem: Friend, newItem: Friend): Boolean {
            return oldItem == newItem
        }
    }

    val swipeGesture = object : SwipeGesture(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val friend = getItem(position)
            when (direction) {
                ItemTouchHelper.LEFT -> {
                    actions.onClickDecline(friend.username2)
                }
                ItemTouchHelper.RIGHT -> {
                    actions.onClickAccept(friend.username2)

                }
            }
        }
    }


}
