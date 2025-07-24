package ro.alingrosu.stockmanagement.data.service

import io.reactivex.rxjava3.core.Single


class AuthMockService : AuthService {

    private val hardcodedUsername = "admin"
    private val hardcodedPassword = "password123"

    override fun authenticate(username: String, password: String): Single<Boolean> {
        return Single.just(username == hardcodedUsername && password == hardcodedPassword)
    }
}