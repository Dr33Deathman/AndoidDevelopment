package kz.kamran.todoapplication.presentation.login.state

sealed class LoginState {
    object Empty: LoginState()
    object Loading: LoginState()
    data class Error(
        val loginMessage: String? = null,
        val passwordMessage: String? = null,
        val message: String? = null
    ): LoginState()
    object Success: LoginState()
}