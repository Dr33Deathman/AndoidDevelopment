package kz.kamran.todoapplication.presentation.local.state

import kz.kamran.todoapplication.data.model.Todo


sealed class LocalTodoListState {
    object Empty : LocalTodoListState()
    object Loading : LocalTodoListState()
    data class Error(val message: String) : LocalTodoListState()
    data class Success(val todoList: List<Todo>) : LocalTodoListState()
}