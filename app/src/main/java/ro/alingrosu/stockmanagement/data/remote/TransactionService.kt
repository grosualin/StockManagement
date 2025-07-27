package ro.alingrosu.stockmanagement.data.remote

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.data.remote.dto.TransactionDto
import java.util.Date
import java.util.concurrent.TimeUnit

interface TransactionService {
    fun postTransaction(transaction: TransactionDto): Completable
    fun fetchAllTransactions(): Single<List<TransactionDto>>
    fun fetchTransactionsByType(type: String): Single<List<TransactionDto>>
    fun fetchTransactionsByProductId(productId: Int): Single<List<TransactionDto>>
    fun fetchRecentTransactions(limit: Int): Single<List<TransactionDto>>
}

class TransactionMockServiceImpl : TransactionService {
    private val mockTransaction = mutableListOf(
        TransactionDto(1, Date().time, "restock", 1, 10, "Initial restock"),
        TransactionDto(2, Date().time, "sale", 2, 5, "Product sale"),
        TransactionDto(3, Date().time, "restock", 3, 7, "Restock"),
        TransactionDto(4, Date().time, "restock", 4, 3, "Restock"),
        TransactionDto(5, Date().time, "sale", 5, 2, "Product sale"),
        TransactionDto(6, Date().time, "sale", 6, 1, "Product sale"),
    )

    override fun postTransaction(transaction: TransactionDto): Completable {
        return Completable.fromAction {
            mockTransaction.add(transaction.copy(id = (mockTransaction.maxOfOrNull { it.id } ?: 0) + 1))
        }
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun fetchAllTransactions(): Single<List<TransactionDto>> {
        return Single.just(mockTransaction.toList())
            .subscribeOn(Schedulers.io())
    }

    override fun fetchTransactionsByType(type: String): Single<List<TransactionDto>> {
        return Single.fromCallable {
            mockTransaction.filter { it.type == type }
        }.subscribeOn(Schedulers.io())
    }

    override fun fetchTransactionsByProductId(productId: Int): Single<List<TransactionDto>> {
        return Single.fromCallable {
            mockTransaction.filter { it.productId == productId }
        }.subscribeOn(Schedulers.io())
    }

    override fun fetchRecentTransactions(limit: Int): Single<List<TransactionDto>> {
        return Single.fromCallable {
            mockTransaction.sortedByDescending { it.date }.take(limit)
        }.subscribeOn(Schedulers.io())
    }

}