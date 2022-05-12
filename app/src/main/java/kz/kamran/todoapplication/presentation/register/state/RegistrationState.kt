package kz.kamran.todoapplication.presentation.register.state

sealed class RegistrationState{
    object Empty: RegistrationState()
    object Loading: RegistrationState()
    data class Error(val message: String): RegistrationState()
    object Success: RegistrationState()
}