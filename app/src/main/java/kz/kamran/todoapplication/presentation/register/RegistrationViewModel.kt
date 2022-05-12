package kz.kamran.todoapplication.presentation.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.kamran.todoapplication.data.remote.RemoteRepository
import kz.kamran.todoapplication.data.remote.dto.RegistrationRequestDto
import kz.kamran.todoapplication.presentation.register.state.RegistrationState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository
) : ViewModel() {
    private var job = Job()
        get() {
            if (field.isCancelled) {
                field = Job()
            }
            return field
        }

    private val _state = MutableLiveData<RegistrationState>(RegistrationState.Empty)
    val state: LiveData<RegistrationState> = _state

    fun cancel() = job.cancel()

    fun register(login: String, password: String) {
        _state.postValue(RegistrationState.Loading)
        val loginError: String? = checkLogin(login)
        val passwordError: String? = checkPassword(password)
        if (!loginError.isNullOrEmpty() || !passwordError.isNullOrEmpty()) {
            _state.postValue(RegistrationState.Error("$loginError $passwordError"))
            return
        }

        val registrationRequest = RegistrationRequestDto(login, password)
        viewModelScope.launch(job) {
            try {
                remoteRepository.register(registrationRequest)
                _state.postValue(RegistrationState.Success)
            } catch (e: Exception) {
                _state.postValue(RegistrationState.Error(e.message.toString()))
            }
        }
    }

    private fun checkPassword(password: String): String? {
        if (password.length in 4..16){
            return null
        }
        return "Invalid password"
    }

    private fun checkLogin(login: String): String? {
        if (login.length in 4..16){
            return null
        }
        return "Invalid login"
    }

}