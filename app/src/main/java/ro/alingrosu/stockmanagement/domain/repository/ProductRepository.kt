package ro.alingrosu.stockmanagement.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Product

interface ProductRepository {
    fun addProduct(product: Product): Completable
    fun updateProduct(product: Product): Completable
    fun deleteProductByProductId(productId: Int): Completable
    fun searchProducts(query: String): Single<List<Product>>
    fun getAllProducts(): Single<List<Product>>
    fun getProductById(id: Int): Maybe<Product>
    fun getLowStockProducts(): Single<List<Product>>
}