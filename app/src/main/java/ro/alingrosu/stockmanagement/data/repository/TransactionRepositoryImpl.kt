package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val dao: TransactionDao) : TransactionRepository {

    override fun addTransaction(transaction: Transaction): Completable =
        dao.insertTransaction(transaction.toEntity())

    override fun getAllTransactions(): Single<List<Transaction>> =
        dao.getAllTransactions().map { it.map { entity -> entity.toDomain() } }

    override fun getTransactionsByType(type: String): Single<List<Transaction>> =
        dao.getTransactionsByType(type).map { it.map { e -> e.toDomain() } }

    override fun getRecentTransactions(limit: Int): Single<List<Transaction>> =
        dao.getRecentTransactions(limit).map { it.map { e -> e.toDomain() } }

    override fun getTransactionsByProductId(productId: Int): Single<List<Transaction>> =
        dao.getTransactionsByProductId(productId).map { it.map { e -> e.toDomain() } }
}