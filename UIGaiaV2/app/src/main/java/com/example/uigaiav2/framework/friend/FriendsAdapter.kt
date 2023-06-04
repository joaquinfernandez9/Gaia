package com.example.uigaiav2.framework.friend

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.uigaiav2.R
import com.example.uigaiav2.databinding.ItemFriendBinding
import com.example.uigaiav2.databinding.ItemTaskBinding
import com.example.uigaiav2.domain.model.Friend
import com.example.uigaiav2.domain.model.Task
import com.example.uigaiav2.domain.model.Tree
import com.example.uigaiav2.domain.model.dto.TaskDTO
import com.example.uigaiav2.framework.swipe.SwipeGesture

class FriendsAdapter(val context: Context, private val actions: Actions) :
    ListAdapter<Tree, FriendsAdapter.FriendsViewHolder>(FriendDiffCallback()) {
    interface Actions {
        fun onClickDetele(friend: String)
    }

    private var selectedItem = mutableListOf<Friend>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsViewHolder {
        return FriendsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_friend, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FriendsViewHolder, position: Int) = with(holder){
        val item = getItem(position)
        bind(item)
    }


    inner class FriendsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemFriendBinding.bind(view)
        fun bind(friendTree: Tree) = with(binding) {
            friendName.text = friendTree.username
            progressBar.progress = friendTree.progress
            friendTreeLevel.text = friendTree.level.toString()
        }
    }

    class FriendDiffCallback : DiffUtil.ItemCallback<Tree>() {
        override fun areItemsTheSame(oldItem: Tree, newItem: Tree): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: Tree, newItem: Tree): Boolean {
            return oldItem == newItem
        }
    }

    val swipeGesture = object: SwipeGesture(context) {
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            when(direction) {
                ItemTouchHelper.LEFT -> {
                    actions.onClickDetele(getItem(viewHolder.adapterPosition).username)
                }
            }
        }
    }

}