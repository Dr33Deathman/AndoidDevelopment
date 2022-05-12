package kz.kamran.todoapplication.presentation.remote.state

import kz.kamran.todoapplication.data.model.Todo

sealed class RemoteListState{
    object Empty: RemoteListState()
    object Loading: RemoteListState()
    object Unauthorized: RemoteListState()
    data class Error(val message: String): RemoteListState()
    data class Success(val list: List<Todo>): RemoteListState()
}