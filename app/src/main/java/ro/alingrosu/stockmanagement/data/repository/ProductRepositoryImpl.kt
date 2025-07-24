package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val dao: ProductDao) : ProductRepository {

    override fun addProduct(product: Product): Completable =
        dao.insertProduct(product.toEntity())

    override fun updateProduct(product: Product): Completable =
        dao.updateProduct(product.toEntity())

    override fun deleteProductByProductId(productId: Int): Completable =
        dao.deleteProductByProductId(productId)

    override fun searchProducts(query: String): Single<List<Product>> =
        dao.searchProducts(query).map { list -> list.map { it.toDomain() } }

    override fun getAllProducts(): Single<List<Product>> =
        dao.getAllProducts().map { list -> list.map { it.toDomain() } }

    override fun getProductById(id: Int): Maybe<Product> =
        dao.getProductById(id).map { it.toDomain() }

    override fun getLowStockProducts(): Single<List<Product>> =
        dao.getLowStockProducts().map { list -> list.map { it.toDomain() } }
}