package kz.kamran.todoapplication.data.remote.interceptor

import kz.kamran.todoapplication.data.remote.provider.UserProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val userProvider: UserProvider
) : Interceptor {
    companion object {
        private const val HEADER_PREFIX_AUTH = "Bearer "
        private const val HEADER_AUTH = "Authorization"
    }

    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var token = ""
        if (userProvider.getToken() != null) {
            token = HEADER_PREFIX_AUTH + userProvider.getToken()
        }
        val request = chain.request().newBuilder()
            .addHeader(HEADER_AUTH, token)
            .build()
        return chain.proceed(request)
    }
}