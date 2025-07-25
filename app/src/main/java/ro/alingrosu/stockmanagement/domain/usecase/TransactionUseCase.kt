package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionWithProduct
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

interface TransactionUseCase {
    fun addTransaction(transaction: Transaction): Completable
    fun getAllTransactions(): Flowable<List<TransactionWithProduct>>
    fun getTransactionsByType(type: String): Flowable<List<TransactionWithProduct>>
    fun getTransactionsByProductId(productId: Int): Flowable<List<TransactionWithProduct>>
    fun getRecentTransactions(limit: Int): Flowable<List<TransactionWithProduct>>
}

class TransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : TransactionUseCase {

    override fun addTransaction(transaction: Transaction): Completable {
        return transactionRepository.addTransaction(transaction)
    }

    override fun getAllTransactions(): Flowable<List<TransactionWithProduct>> {
        return transactionRepository.getAllTransactions()
    }

    override fun getTransactionsByType(type: String): Flowable<List<TransactionWithProduct>> {
        return transactionRepository.getTransactionsByType(type)
    }

    override fun getTransactionsByProductId(productId: Int): Flowable<List<TransactionWithProduct>> {
        return transactionRepository.getTransactionsByProductId(productId)
    }

    override fun getRecentTransactions(limit: Int): Flowable<List<TransactionWithProduct>> {
        return transactionRepository.getRecentTransactions(limit)
    }
}