package ro.alingrosu.stockmanagement.data.service

import io.reactivex.rxjava3.core.Single


interface AuthService {

    fun authenticate(username: String, password: String): Single<Boolean>
}