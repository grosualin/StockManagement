package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.service.ProductService
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val localDataSource: ProductDao,
    private val remoteDataSource: ProductService
) : ProductRepository {

    override fun addProduct(product: Product): Completable {
        return remoteDataSource.postProduct(product.toDto())
            .andThen(localDataSource.insertProduct(product.toEntity()).onErrorComplete())
    }

    override fun updateProduct(product: Product): Completable {
        return remoteDataSource.updateProduct(product.toDto())
            .andThen(localDataSource.updateProduct(product.toEntity()).onErrorComplete())
    }

    override fun deleteProductByProductId(productId: Int): Completable {
        return remoteDataSource.deleteProductById(productId)
            .andThen(localDataSource.deleteProductById(productId).onErrorComplete())
    }

    override fun searchProducts(query: String): Flowable<List<Product>> {
        val local = localDataSource.searchProducts(query).map { products -> products.map { it.toDomain() } }
        val remote = remoteDataSource.searchProducts(query)
            .map { product -> product.map { it.toDomain() } }
        return Single.concatArrayEager(local, remote)
    }

    override fun getAllProducts(): Flowable<List<Product>> {
        val local = localDataSource.getAllProducts().map { products -> products.map { it.toDomain() } }
        val remote = remoteDataSource.fetchAllProducts()
            .doOnSuccess { products -> localDataSource.insertAll(products.map { it.toEntity() }).subscribe() }
            .map { product -> product.map { it.toDomain() } }
        return Single.concatArrayEager(local, remote)
    }

    override fun getProductById(id: Int): Flowable<Product> {
        val local = localDataSource.getProductById(id).map { it.toDomain() }
        val remote = remoteDataSource.fetchProductById(id)
            .doOnSuccess { product -> localDataSource.updateProduct(product.toEntity()).subscribe() }
            .map { product -> product.toDomain() }
        return Maybe.concatArrayEager(local, remote)
    }

    override fun getLowStockProducts(): Flowable<List<Product>> {
        val local = localDataSource.getLowStockProducts().map { products -> products.map { it.toDomain() } }
        val remote = remoteDataSource.fetchLowStockProducts()
            .doOnSuccess { products -> localDataSource.insertAll(products.map { it.toEntity() }).subscribe() }
            .map { product -> product.map { it.toDomain() } }
        return Single.concatArrayEager(local, remote)
    }
}