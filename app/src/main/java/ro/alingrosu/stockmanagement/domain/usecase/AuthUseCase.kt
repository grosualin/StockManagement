package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.repository.AuthRepository
import javax.inject.Inject

interface AuthUseCase {
    fun authenticate(username: String, password: String): Single<Boolean>
}

class AuthUseCaseImpl @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun authenticate(username: String, password: String): Single<Boolean> {
        return authRepository.authenticate(username, password)
    }
}