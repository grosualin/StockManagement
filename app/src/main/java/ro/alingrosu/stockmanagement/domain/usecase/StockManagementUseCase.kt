package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

interface StockManagementUseCase {
    fun increaseStock(productId: Int, quantity: Int): Completable
    fun decreaseStock(productId: Int, quantity: Int): Completable
    fun checkStockLevel(productId: Int): Single<Boolean>
}

class StockManagementUseCaseImpl(
    private val productRepository: ProductRepository
) : StockManagementUseCase {
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

    override fun checkStockLevel(productId: Int): Single<Boolean> {
        return productRepository.getProductById(productId)
            .toSingle()
            .map { it.currentStock < it.minStock }
    }
}