package kz.kamran.todoapplication.presentation.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.data.remote.RemoteRepository
import kz.kamran.todoapplication.data.remote.mapper.toReversedTodo
import kz.kamran.todoapplication.data.remote.mapper.toCreateRequestDto
import kz.kamran.todoapplication.data.remote.mapper.toUpdateRequestDto
import kz.kamran.todoapplication.data.remote.provider.UserProvider
import kz.kamran.todoapplication.exception.UnauthorizedException
import kz.kamran.todoapplication.presentation.remote.state.RemoteListState
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val userProvider: UserProvider
) : ViewModel() {

    private var job = Job()
        get() {
            if (field.isCancelled) {
                field = Job()
            }
            return field
        }

    private val _state = MutableLiveData<RemoteListState>(RemoteListState.Empty)
    val state: LiveData<RemoteListState> = _state

    fun cancel() = job.cancel()

    fun getTodoList() {
        _state.postValue(RemoteListState.Loading)
        viewModelScope.launch(job) {
            try {
                val list = remoteRepository.getTodoList()
                _state.postValue(RemoteListState.Success(list))
            } catch (e: Exception) {
                if (e is UnauthorizedException) {
                    _state.postValue(RemoteListState.Unauthorized)
                } else {
                    _state.postValue(RemoteListState.Error("Server error"))
                }
            }
        }
    }

    fun changeState(todo: Todo) {
        viewModelScope.launch(job) {
            _state.postValue(RemoteListState.Loading)
            try {
                val updatedTodo = todo.toReversedTodo()
                val todoRequest = updatedTodo.toUpdateRequestDto()
                remoteRepository.updateTodo(todoRequest)
                getTodoList()
            } catch (e: Exception) {
                if (e is UnauthorizedException) {
                    _state.postValue(RemoteListState.Unauthorized)
                } else {
                    _state.postValue(RemoteListState.Error("Server error"))
                }
            }
        }
    }

    fun logout() = userProvider.saveToken(null)

}