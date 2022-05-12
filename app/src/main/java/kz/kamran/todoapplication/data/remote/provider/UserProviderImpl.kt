package kz.kamran.todoapplication.data.remote.provider

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : UserProvider {
    companion object {
        private const val PREF_NAME = "KAMRAN_SHARED_PREF"
        private const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"
    }

    private var sharedPreferences: SharedPreferences? = null
        get() {
            if (field == null)
                field = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return field
        }

    override fun getToken(): String? = sharedPreferences?.getString(
        KEY_AUTH_TOKEN, null
    )

    override fun saveToken(token: String?) {
        val preferences = sharedPreferences
        val editor = preferences?.edit()
        editor?.putString(KEY_AUTH_TOKEN, token)
        editor?.apply()
    }
}
