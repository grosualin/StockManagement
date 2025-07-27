package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionType
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

interface StockManagementUseCase {
    fun recordStockTransaction(transaction: Transaction): Completable
    fun increaseStock(productId: Int, quantity: Int): Completable
    fun decreaseStock(productId: Int, quantity: Int): Completable
    fun checkStockLevel(productId: Int): Flowable<Boolean>
    fun getLowStockProducts(): Flowable<List<Product>>
    fun getAllProducts(): Flowable<List<Product>>
    fun searchProductBarcode(barcode: String): Maybe<Product>
}

class StockManagementUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : StockManagementUseCase {

    override fun recordStockTransaction(transaction: Transaction): Completable {
        return when (transaction.type) {
            TransactionType.RESTOCK -> increaseStock(transaction.product.id, transaction.quantity)
            TransactionType.SALE -> decreaseStock(transaction.product.id, transaction.quantity)
        }
            .andThen(
                transactionRepository.addTransaction(transaction)
            )
    }

    override fun increaseStock(productId: Int, quantity: Int): Completable {
        return productRepository.getProductById(productId)
            .flatMapCompletable { product ->
                val newStock = product.currentStock + quantity
                productRepository.updateProduct(product.copy(currentStock = newStock))
            }
    }

    override fun decreaseStock(productId: Int, quantity: Int): Completable {
        return productRepository.getProductById(productId)
            .flatMapCompletable { product ->
                val newStock = product.currentStock - quantity
                if (newStock < 0) Completable.error(IllegalStateException("Insufficient stock"))
                else productRepository.updateProduct(product.copy(currentStock = newStock))
            }
    }

    override fun checkStockLevel(productId: Int): Flowable<Boolean> {
        return productRepository.getProductById(productId)
            .map { it.currentStock < it.minStock }
    }

    override fun getLowStockProducts(): Flowable<List<Product>> {
        return productRepository.getLowStockProducts()
    }

    override fun getAllProducts(): Flowable<List<Product>> {
        return productRepository.getAllProducts()
    }

    override fun searchProductBarcode(barcode: String): Maybe<Product> {
        return productRepository.searchProductBarcode(barcode)
    }
}