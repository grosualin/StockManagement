package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

interface ProductUseCase {
    fun addProduct(product: Product): Completable
    fun updateProduct(product: Product): Completable
    fun deleteProductByProductId(id: Int): Completable
    fun searchProducts(query: String): Flowable<List<Product>>
    fun getAllProducts(): Flowable<List<Product>>
    fun getProductById(id: Int): Flowable<Product>
}

class ProductUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductUseCase {
    override fun addProduct(product: Product): Completable {
        return productRepository.addProduct(product)
    }

    override fun updateProduct(product: Product): Completable {
        return productRepository.updateProduct(product)
    }

    override fun deleteProductByProductId(id: Int): Completable {
        return productRepository.deleteProductByProductId(id)
    }

    override fun searchProducts(query: String): Flowable<List<Product>> {
        return productRepository.searchProducts(query)
    }

    override fun getAllProducts(): Flowable<List<Product>> {
        return productRepository.getAllProducts()
    }

    override fun getProductById(id: Int): Flowable<Product> {
        return productRepository.getProductById(id)
    }
}