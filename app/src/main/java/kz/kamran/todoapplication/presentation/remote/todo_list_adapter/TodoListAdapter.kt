package kz.kamran.todoapplication.presentation.remote.todo_list_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.databinding.TodoItemBinding

class TodoListAdapter : RecyclerView.Adapter<TodoViewHolder>() {

    var onCompleteClick: ((Todo) -> Unit)? = null
    var onItemClick: ((Todo) -> Unit)? = null

    var todoList: List<Todo> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        with(holder) {
            titleTextView.text = todo.title
            descriptionTextView.text = todo.description
            deadlineTextView.text = todo.deadline.toString()
            isCompletedCheckBox.isChecked = todo.isCompleted
            isCompletedCheckBox.setOnClickListener {
                if (todo.isCompleted != isCompletedCheckBox.isChecked) {
                    onCompleteClick?.invoke(todo)
                }
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(todo)
            }
        }
    }

    override fun getItemCount() = todoList.size
}

