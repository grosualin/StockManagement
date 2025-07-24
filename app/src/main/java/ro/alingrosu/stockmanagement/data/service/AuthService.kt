package ro.alingrosu.stockmanagement.data.service

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers


interface AuthService {

    fun authenticate(username: String, password: String): Single<Boolean>
}

class AuthMockServiceImpl : AuthService {

    private val hardcodedUsername = "admin"
    private val hardcodedPassword = "open"

    override fun authenticate(username: String, password: String): Single<Boolean> {
        return Single.just(username == hardcodedUsername && password == hardcodedPassword)
            .subscribeOn(Schedulers.io())
    }
}