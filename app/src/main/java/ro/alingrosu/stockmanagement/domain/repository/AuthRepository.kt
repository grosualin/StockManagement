package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Single

interface AuthRepository {
    fun authenticate(username: String, password: String): Single<Boolean>
}