package kz.kamran.todoapplication.presentation.remote.todo_list_adapter

import androidx.recyclerview.widget.RecyclerView
import kz.kamran.todoapplication.databinding.TodoItemBinding

class TodoViewHolder(binding: TodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val titleTextView = binding.titleTextView
    val descriptionTextView = binding.descriptionTextView
    val deadlineTextView = binding.deadlineTextView
    val isCompletedCheckBox = binding.isCompletedCheckBox
}