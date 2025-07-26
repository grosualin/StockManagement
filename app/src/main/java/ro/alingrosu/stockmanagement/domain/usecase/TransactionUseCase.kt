package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

interface TransactionUseCase {
    fun addTransaction(transaction: Transaction): Completable
    fun searchTransactions(query: String): Flowable<List<Transaction>>
    fun getAllTransactions(): Flowable<List<Transaction>>
    fun getTransactionsByType(type: String): Flowable<List<Transaction>>
    fun getTransactionsByProductId(productId: Int): Flowable<List<Transaction>>
    fun getRecentTransactions(limit: Int): Flowable<List<Transaction>>
}

class TransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : TransactionUseCase {

    override fun addTransaction(transaction: Transaction): Completable {
        return transactionRepository.addTransaction(transaction)
    }

    override fun searchTransactions(query: String): Flowable<List<Transaction>> {
        return transactionRepository.searchTransactions(query)
    }

    override fun getAllTransactions(): Flowable<List<Transaction>> {
        return transactionRepository.getAllTransactions()
    }

    override fun getTransactionsByType(type: String): Flowable<List<Transaction>> {
        return transactionRepository.getTransactionsByType(type)
    }

    override fun getTransactionsByProductId(productId: Int): Flowable<List<Transaction>> {
        return transactionRepository.getTransactionsByProductId(productId)
    }

    override fun getRecentTransactions(limit: Int): Flowable<List<Transaction>> {
        return transactionRepository.getRecentTransactions(limit)
    }
}