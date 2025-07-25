package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionWithProduct

interface TransactionRepository {
    fun addTransaction(transaction: Transaction): Completable
    fun getAllTransactions(): Flowable<List<TransactionWithProduct>>
    fun getTransactionsByType(type: String): Flowable<List<TransactionWithProduct>>
    fun getRecentTransactions(limit: Int): Flowable<List<TransactionWithProduct>>
    fun getTransactionsByProductId(productId: Int): Flowable<List<TransactionWithProduct>>
}