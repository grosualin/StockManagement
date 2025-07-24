package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.service.TransactionService
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionDao,
    private val remoteDataSource: TransactionService
) : TransactionRepository {

    override fun addTransaction(transaction: Transaction): Completable {
        return remoteDataSource.postTransaction(transaction.toDto())
            .andThen(localDataSource.insertTransaction(transaction.toEntity()))
    }

    override fun getAllTransactions(): Flowable<List<Transaction>> {
        val local = localDataSource.getAllTransactions().map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSource.fetchAllTransactions()
            .doOnSuccess { transactions -> localDataSource.insertAll(transactions.map { it.toEntity() }).subscribe() }
            .map { transactions -> transactions.map { it.toDomain() } }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getTransactionsByType(type: String): Flowable<List<Transaction>> {
        val local = localDataSource.getTransactionsByType(type).map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSource.fetchTransactionsByType(type)
            .doOnSuccess { transactions -> localDataSource.insertAll(transactions.map { it.toEntity() }).subscribe() }
            .map { transaction -> transaction.map { it.toDomain() } }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getRecentTransactions(limit: Int): Flowable<List<Transaction>> {
        val local = localDataSource.getRecentTransactions(limit).map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSource.fetchRecentTransactions(limit)
            .doOnSuccess { transactions -> localDataSource.insertAll(transactions.map { it.toEntity() }).subscribe() }
            .map { transactions -> transactions.map { it.toDomain() } }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getTransactionsByProductId(productId: Int): Flowable<List<Transaction>> {
        val local =
            localDataSource.getTransactionsByProductId(productId).map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSource.fetchTransactionsByProductId(productId)
            .doOnSuccess { transactions -> localDataSource.insertAll(transactions.map { it.toEntity() }).subscribe() }
            .map { transactions -> transactions.map { it.toDomain() } }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }
}