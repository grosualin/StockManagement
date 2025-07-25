package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.service.ProductService
import ro.alingrosu.stockmanagement.data.service.SupplierService
import ro.alingrosu.stockmanagement.data.service.TransactionService
import ro.alingrosu.stockmanagement.data.service.dto.ProductDto
import ro.alingrosu.stockmanagement.data.service.dto.TransactionDto
import ro.alingrosu.stockmanagement.domain.model.Product
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
     * This extension function is used to map the list of products to a list of product with supplier.
     * Since the local implementation of service does not provide complete information about the product,
     * including supplier information, we need to fetch the supplier information from the remote service in a separate api call.
     * Ideally, the list of proructs coming from backend would include information about the supplier as well,
     * not just supplier id.
     */
    private fun Maybe<ProductDto>.mapToProductWithSupplier(): Maybe<Product> {
        return this
            .flatMapSingle { product ->
                remoteDataSourceSupplier.fetchSupplierById(product.supplierId)
                    .switchIfEmpty(Single.error(IllegalStateException("Supplier not found")))
                    .map { supplier ->
                        product.toDomain(supplier)
                    }
            }
    }

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
    }

    override fun addTransaction(transaction: Transaction): Completable {
        return remoteDataSourceTransaction.postTransaction(transaction.toDto())
            .andThen(localDataSourceTransaction.insertTransaction(transaction.toEntity()))
    }

    override fun getAllTransactions(): Flowable<List<Transaction>> {
        val local = localDataSourceTransaction.getAllTransactions().map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSourceTransaction.fetchAllTransactions()
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getTransactionsByType(type: String): Flowable<List<Transaction>> {
        val local =
            localDataSourceTransaction.getTransactionsByType(type).map { transactions -> transactions.map { it.toDomain() } }.toFlowable()
        val remote = remoteDataSourceTransaction.fetchTransactionsByType(type)
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getRecentTransactions(limit: Int): Flowable<List<Transaction>> {
        val local = localDataSourceTransaction.getRecentTransactions(limit)
            .map { transactions -> transactions.map { it.toDomain() } }
            .toFlowable()
        val remote = remoteDataSourceTransaction.fetchRecentTransactions(limit)
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }

    override fun getTransactionsByProductId(productId: Int): Flowable<List<Transaction>> {
        val local =
            localDataSourceTransaction.getTransactionsByProductId(productId)
                .map { transactions -> transactions.map { it.toDomain() } }
                .toFlowable()
        val remote = remoteDataSourceTransaction.fetchTransactionsByProductId(productId)
            .mapToTransactionWithProduct()
            .doOnSuccess { transactionsWithProducts -> updateDb(transactionsWithProducts).subscribe() }
            .toFlowable()
        return Flowable.concatArrayEager(local, remote)
    }
}