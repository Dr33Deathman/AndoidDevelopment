package kz.kamran.todoapplication.presentation.add_edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.kamran.todoapplication.data.local.LocalRepository
import kz.kamran.todoapplication.data.model.Category
import kz.kamran.todoapplication.data.model.Todo
import kz.kamran.todoapplication.data.remote.RemoteRepository
import kz.kamran.todoapplication.data.remote.dto.CategoryRequestDto
import kz.kamran.todoapplication.data.remote.dto.TodoRequestDto
import kz.kamran.todoapplication.data.remote.dto.TodoUpdateRequestDto
import kz.kamran.todoapplication.presentation.add_edit.state.TodoState
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    private var job = Job()
        get() {
            if (field.isCancelled) field = Job()
            return field
        }

    private lateinit var categoryList: List<Category>

    private val _state = MutableLiveData<TodoState>(TodoState.Empty)
    val state: LiveData<TodoState> = _state

    fun cancel() = job.cancel()

    suspend fun getCategoryList(isLocal: Boolean) {
        categoryList = if (isLocal) {
            localRepository.getCategoryList()
        } else {
            remoteRepository.getCategoryList()
        }
    }

    fun saveTodo(
        title: String,
        description: String,
        categoryTitle: String,
        calendar: Calendar,
        isLocal: Boolean,
        id: Int,
    ) {
        _state.postValue(TodoState.Loading)
        viewModelScope.launch(job) {
            try {
                getCategoryList(isLocal)
                if (calendar.before(Calendar.getInstance())) {
                    _state.postValue(TodoState.Error("Deadline can't be before current time"))
                    return@launch
                }

                val categoryId =
                    categoryList.find { categoryTitle.equals(it.title, ignoreCase = true) }?.id ?: 0

                if (isLocal) {
                    val category = Category(id = categoryId, title = categoryTitle)
                    val todoEntity = Todo(
                        id = id,
                        title = title,
                        description = description,
                        deadline = calendar.time,
                        category = category,
                        isCompleted = false
                    )
                    localRepository.saveTodo(todoEntity)
                } else {
                    if (categoryId == 0) {
                        remoteRepository.saveCategory(CategoryRequestDto(title = categoryTitle))
                    }
                    val remoteCategoryId =
                        remoteRepository.getCategoryList().find { it.title == categoryTitle }!!.id
                    if (id == 0) {
                        remoteRepository.saveTodo(
                            TodoRequestDto(
                                categoryId = remoteCategoryId,
                                title = title,
                                description = description,
                                isCompleted = false,
                                deadline = calendar.time.time
                            )
                        )
                    } else {
                        remoteRepository.updateTodo(
                            TodoUpdateRequestDto(
                                id = id,
                                categoryId = remoteCategoryId,
                                title = title,
                                description = description,
                                isCompleted = false,
                                deadline = calendar.time.time
                            )
                        )
                    }
                }
                _state.postValue(TodoState.Success)
            } catch (e: Exception) {
                _state.postValue(TodoState.Error(e.message.toString()))
            }
        }
    }

}