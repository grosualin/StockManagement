package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.domain.model.Product

interface ProductRepository {
    fun addProduct(product: Product): Completable
    fun updateProduct(product: Product): Completable
    fun deleteProductByProductId(productId: Int): Completable
    fun searchProducts(query: String): Flowable<List<Product>>
    fun getAllProducts(): Flowable<List<Product>>
    fun getProductById(id: Int): Flowable<Product>
    fun getLowStockProducts(): Flowable<List<Product>>
}