package kz.kamran.todoapplication.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kz.kamran.todoapplication.data.remote.RemoteRepository
import kz.kamran.todoapplication.data.remote.dto.LoginRequestDto
import kz.kamran.todoapplication.presentation.login.state.LoginState
import java.lang.Error
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
) : ViewModel() {

    private val _loginState = MutableLiveData<LoginState>(LoginState.Empty)
    val loginState: LiveData<LoginState> get() = _loginState

    private var job = Job()
        get() {
            if (field.isCancelled) {
                field = Job()
            }
            return field
        }

    init {
        _loginState.postValue(LoginState.Loading)
        if (!remoteRepository.isLogged()) {
            _loginState.postValue(LoginState.Success)
        } else {
            _loginState.postValue(LoginState.Empty)
        }
    }

    fun login(login: String, password: String) {
        _loginState.postValue(LoginState.Loading)
        val loginError = checkLogin(login)
        val passwordError = checkPassword(password)

        if (!loginError.isNullOrEmpty() || !passwordError.isNullOrEmpty()) {
            _loginState.postValue(
                LoginState.Error(
                    loginMessage = loginError,
                    passwordMessage = passwordError
                )
            )
            return
        }
        val loginRequestDto = LoginRequestDto(login = login, password = password)
        viewModelScope.launch(job) {
            try {
                remoteRepository.login(loginRequestDto)
                _loginState.postValue(LoginState.Success)
                delay(500L)
                _loginState.postValue(LoginState.Empty)
            } catch (e: Exception) {
                _loginState.postValue(LoginState.Error(e.message.toString()))
            }
        }
    }

    private fun checkPassword(password: String): String? =
        if (password.length in 4..16) {
            null
        } else {
            "Invalid password"
        }


    private fun checkLogin(login: String): String? =
        if (login.length in 4..16) {
            null
        } else {
            "Invalid login"
        }

    fun cancel() = job.cancel()
}