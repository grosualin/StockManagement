package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Transaction

interface TransactionRepository {
    fun addTransaction(transaction: Transaction): Completable
    fun getAllTransactions(): Single<List<Transaction>>
    fun getTransactionsByType(type: String): Single<List<Transaction>>
    fun getRecentTransactions(limit: Int): Single<List<Transaction>>
    fun getTransactionsByProductId(productId: Int): Single<List<Transaction>>
}