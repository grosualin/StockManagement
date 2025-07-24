package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

interface TransactionUseCase {
    fun addTransaction(transaction: Transaction): Completable
    fun getAllTransactions(): Single<List<Transaction>>
    fun getTransactionsByType(type: String): Single<List<Transaction>>
    fun getTransactionsByProductId(productId: Int): Single<List<Transaction>>
    fun getRecentTransactions(limit: Int): Single<List<Transaction>>
}

class TransactionUseCaseImpl(
    private val transactionRepository: TransactionRepository
) : TransactionUseCase {

    override fun addTransaction(transaction: Transaction) = transactionRepository.addTransaction(transaction)
    override fun getAllTransactions() = transactionRepository.getAllTransactions()
    override fun getTransactionsByType(type: String): Single<List<Transaction>> = transactionRepository.getTransactionsByType(type)
    override fun getTransactionsByProductId(productId: Int): Single<List<Transaction>> =
        transactionRepository.getTransactionsByProductId(productId)

    override fun getRecentTransactions(limit: Int): Single<List<Transaction>> = transactionRepository.getRecentTransactions(limit)
}