package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

interface ProductUseCase {
    fun addProduct(product: Product): Completable
    fun updateProduct(product: Product): Completable
    fun deleteProductByProductId(id: Int): Completable
    fun searchProducts(query: String): Single<List<Product>>
    fun getAllProducts(): Single<List<Product>>
    fun getProductById(id: Int): Maybe<Product>
}

class ProductUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : ProductUseCase {
    override fun addProduct(product: Product) = productRepository.addProduct(product)
    override fun updateProduct(product: Product) = productRepository.updateProduct(product)
    override fun deleteProductByProductId(id: Int) = productRepository.deleteProductByProductId(id)
    override fun searchProducts(query: String) = productRepository.searchProducts(query)
    override fun getAllProducts() = productRepository.getAllProducts()
    override fun getProductById(id: Int) = productRepository.getProductById(id)
}