package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.remote.AuthService
import ro.alingrosu.stockmanagement.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) : AuthRepository {

    override fun authenticate(username: String, password: String): Single<Boolean> {
        return authService.authenticate(username, password)
    }
}