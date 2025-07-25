package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.service.ProductService
import ro.alingrosu.stockmanagement.data.service.TransactionService
import ro.alingrosu.stockmanagement.data.service.dto.TransactionDto
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionWithProduct
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSourceTransaction: TransactionDao,
    private val localDataSourceProduct: ProductDao,
    private val remoteDataSourceTransaction: TransactionService,
    private val remoteDataSourceProduct: ProductService
) : TransactionRepository {

    /**
     * This extension function is used to map the list of transactions to a list of transaction with product.
     * Since the local implementation of service does not provide complete information about the transaction,
     * including product information, we need to fetch the product information from the remote service in a separate api call.
     * Ideally, the list of transaction coming from backend would include information about the product as well,
     * not just product id.
     */
    private fun Single<List<TransactionDto>>.mapToTransactionWithProductList(): Single<List<TransactionWithProduct>> {
        return this
            .flattenAsObservable { it }
            .flatMapSingle { transaction ->
                remoteDataSourceProduct.fetchProductById(transaction.productId)
                    .switchIfEmpty(Single.error(IllegalStateException("Product not found")))
                    .map { product ->
                        TransactionWithProduct(transaction.toDomain(), product.toDomain())
                    }
            }
            .toList()
    }

    private fun updateDb(transactions: List<TransactionWithProduct>): Completable {
        return localDataSourceProduct.insertAll(transactions.map { it.product.toEntity() })
            .andThen(localDataSourceTransaction.insertAll(transactions.map { it.transaction.toEntity() }))
    }

    override fun addTransaction(transaction: Transaction): Completable {
        return remoteDataSourceTransaction.postTransaction(transaction.toDto())
            .andThen(localDataSourceTransaction.insertTransaction(transaction.toEntity()))
    }

    override fun getAllTransactions(): Flowable<List<TransactionWithProduct>> {
        val local = localDataSourceTransaction.getAllTransactions().map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSourceTransaction.fetchAllTransactions()
            .mapToTransactionWithProductList()
            .doOnSuccess { transactionsWithProducts ->
                updateDb(transactionsWithProducts).subscribe()
            }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getTransactionsByType(type: String): Flowable<List<TransactionWithProduct>> {
        val local =
            localDataSourceTransaction.getTransactionsByType(type).map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSourceTransaction.fetchTransactionsByType(type)
            .mapToTransactionWithProductList()
            .doOnSuccess { transactionsWithProducts ->
                updateDb(transactionsWithProducts).subscribe()
            }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getRecentTransactions(limit: Int): Flowable<List<TransactionWithProduct>> {
        val local = localDataSourceTransaction.getRecentTransactions(limit)
            .map { transactions ->
                transactions.map { it.toDomain() }
            }
            .toFlowable()
        val remote = remoteDataSourceTransaction.fetchRecentTransactions(limit)
            .mapToTransactionWithProductList()
            .doOnSuccess { transactionsWithProducts ->
                updateDb(transactionsWithProducts).subscribe()
            }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getTransactionsByProductId(productId: Int): Flowable<List<TransactionWithProduct>> {
        val local =
            localDataSourceTransaction.getTransactionsByProductId(productId).map { transactions -> transactions.map { it.toDomain() } }
                .toFlowable()
        val remote = remoteDataSourceTransaction.fetchTransactionsByProductId(productId)
            .mapToTransactionWithProductList()
            .doOnSuccess { transactionsWithProducts ->
                updateDb(transactionsWithProducts).subscribe()
            }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }
}