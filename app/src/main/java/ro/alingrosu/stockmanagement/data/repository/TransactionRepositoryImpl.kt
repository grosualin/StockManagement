package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.service.ProductService
import ro.alingrosu.stockmanagement.data.service.SupplierService
import ro.alingrosu.stockmanagement.data.service.TransactionService
import ro.alingrosu.stockmanagement.data.service.dto.TransactionDto
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSourceTransaction: TransactionDao,
    private val localDataSourceProduct: ProductDao,
    private val localDataSourceSupplier: SupplierDao,
    private val remoteDataSourceTransaction: TransactionService,
    private val remoteDataSourceProduct: ProductService,
    private val remoteDataSourceSupplier: SupplierService
) : TransactionRepository {

    /**
     * This extension function is used to map the list of transactions to a list of transaction with product.
     * Since the local implementation of service does not provide complete information about the transaction,
     * including product information, we need to fetch the product information from the remote service in a separate api call.
     * Ideally, the list of transaction coming from backend would include information about the product as well,
     * not just product id.
     */
    private fun Single<List<TransactionDto>>.mapToTransactionWithProduct(): Single<List<Transaction>> {
        return this
            .flattenAsObservable { it }
            .flatMapSingle { transaction ->
                remoteDataSourceProduct.fetchProductById(transaction.productId)
                    .switchIfEmpty(Single.error(IllegalStateException("Product not found")))
                    .flatMap { productDto ->
                        remoteDataSourceSupplier.fetchSupplierById(productDto.supplierId)
                            .switchIfEmpty(Single.error(IllegalStateException("Product not found")))
                            .map { supplierDto -> transaction.toDomain(productDto, supplierDto) }
                    }
            }
            .toList()
    }

    private fun updateDb(transactions: List<Transaction>): Completable {
        return localDataSourceSupplier.insertAll(transactions.map { it.product.supplier.toEntity() })
            .andThen(localDataSourceProduct.insertAll(transactions.map { it.product.toEntity() }))
            .andThen(localDataSourceTransaction.insertAll(transactions.map { it.toEntity() }))
            .subscribeOn(Schedulers.io())
    }

    override fun addTransaction(transaction: Transaction): Completable {
        return remoteDataSourceTransaction.postTransaction(transaction.toDto())
            .andThen(
                localDataSourceTransaction.getMaxId()
                    .map { maxId -> maxId + 1 }
                    .flatMapCompletable { nextId ->
                        val transactionWithId = transaction.toEntity().copy(id = nextId)
                        localDataSourceTransaction.insertTransaction(transactionWithId).onErrorComplete()
                    }
            )
            .subscribeOn(Schedulers.io())
    }

    override fun searchTransactions(query: String): Flowable<List<Transaction>> {
        return localDataSourceTransaction.searchTransactions(query)
            .subscribeOn(Schedulers.io())
            .map { transactions -> transactions.map { it.toDomain() }.sortedBy { it.id } }
            .toFlowable()
    }

    override fun getAllTransactions(): Flowable<List<Transaction>> {
        val local = localDataSourceTransaction.getAllTransactions()
            .subscribeOn(Schedulers.io())
            .map { transactions -> transactions.map { it.toDomain() } }
        val remote = remoteDataSourceTransaction.fetchAllTransactions()
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedByDescending { transaction -> transaction.date } }
    }

    override fun getTransactionsByType(type: String): Flowable<List<Transaction>> {
        val local = localDataSourceTransaction.getTransactionsByType(type)
            .subscribeOn(Schedulers.io())
            .map { transactions -> transactions.map { it.toDomain() } }
        val remote = remoteDataSourceTransaction.fetchTransactionsByType(type)
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedByDescending { transaction -> transaction.date } }
    }

    override fun getRecentTransactions(limit: Int): Flowable<List<Transaction>> {
        val local = localDataSourceTransaction.getRecentTransactions(limit)
            .subscribeOn(Schedulers.io())
            .map { transactions -> transactions.map { it.toDomain() } }
        val remote = remoteDataSourceTransaction.fetchRecentTransactions(limit)
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedByDescending { transaction -> transaction.date } }
    }

    override fun getTransactionsByProductId(productId: Int): Flowable<List<Transaction>> {
        val local = localDataSourceTransaction.getTransactionsByProductId(productId)
            .subscribeOn(Schedulers.io())
            .map { transactions -> transactions.map { it.toDomain() } }
        val remote = remoteDataSourceTransaction.fetchTransactionsByProductId(productId)
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedByDescending { transaction -> transaction.date } }
    }
}