package kz.kamran.todoapplication.presentation.add_edit.state

sealed class TodoState {
    object Empty: TodoState()
    object Loading: TodoState()
    data class Error(val message: String): TodoState()
    object Success: TodoState()
}