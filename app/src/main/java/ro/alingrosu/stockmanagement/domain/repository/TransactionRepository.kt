package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Transaction

interface TransactionRepository {
    fun addTransaction(transaction: Transaction): Completable
    fun getAllTransactions(): Flowable<List<Transaction>>
    fun getTransactionsByType(type: String): Flowable<List<Transaction>>
    fun getRecentTransactions(limit: Int): Flowable<List<Transaction>>
    fun getTransactionsByProductId(productId: Int): Flowable<List<Transaction>>
}