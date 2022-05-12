package kz.kamran.todoapplication.data.remote.provider

interface UserProvider {
    fun getToken(): String?
    fun saveToken(token: String?)
}