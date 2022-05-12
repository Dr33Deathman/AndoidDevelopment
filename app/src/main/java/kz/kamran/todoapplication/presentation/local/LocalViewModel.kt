package kz.kamran.todoapplication.presentation.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.kamran.todoapplication.data.local.LocalRepository
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.data.remote.mapper.toReversedTodo
import kz.kamran.todoapplication.presentation.local.state.LocalTodoListState
import javax.inject.Inject

@HiltViewModel
class LocalViewModel @Inject constructor(
    private val localRepository: LocalRepository
) : ViewModel() {

    private var job = Job()
        get() {
            if (field.isCancelled) {
                field = Job()
            }
            return field
        }

    private val _todoListState = MutableLiveData<LocalTodoListState>(LocalTodoListState.Empty)
    val todoListState: LiveData<LocalTodoListState> get() = _todoListState

    fun getLocalTodoList() {
        _todoListState.postValue(LocalTodoListState.Loading)
        viewModelScope.launch(job) {
            val todoList = localRepository.getTodoList()
            _todoListState.postValue(LocalTodoListState.Success(todoList))
        }
    }

    fun changeState(todo: Todo) {
        _todoListState.postValue(LocalTodoListState.Loading)
        viewModelScope.launch(job) {
            localRepository.saveTodo(todo.toReversedTodo())
            getLocalTodoList()
        }
    }
    fun cancel() = job.cancel()
}