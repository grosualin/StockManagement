package ro.alingrosu.stockmanagement.data.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.mapper.toDomain
import ro.alingrosu.stockmanagement.data.mapper.toDto
import ro.alingrosu.stockmanagement.data.mapper.toEntity
import ro.alingrosu.stockmanagement.data.remote.ProductService
import ro.alingrosu.stockmanagement.data.remote.SupplierService
import ro.alingrosu.stockmanagement.data.remote.dto.ProductDto
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val localDataSourceProduct: ProductDao,
    private val localDataSourceSupplier: SupplierDao,
    private val remoteDataSourceProduct: ProductService,
    private val remoteDataSourceSupplier: SupplierService,
) : ProductRepository {

    /**
     * This extension function is used to map the list of products to a list of product with supplier.
     * Since the local implementation of service does not provide complete information about the product,
     * including supplier information, we need to fetch the supplier information from the remote service in a separate api call.
     * Ideally, the list of proructs coming from backend would include information about the supplier as well,
     * not just supplier id.
     */
    private fun Single<List<ProductDto>>.mapToProductWithSupplier(): Single<List<Product>> {
        return this
            .flattenAsObservable { it }
            .flatMapSingle { product ->
                remoteDataSourceSupplier.fetchSupplierById(product.supplierId)
                    .switchIfEmpty(Single.error(IllegalStateException("Supplier not found")))
                    .map { supplier ->
                        product.toDomain(supplier)
                    }
            }
            .toList()
    }

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

    private fun updateDb(products: List<Product>): Completable {
        return localDataSourceSupplier.insertAll(products.map { it.supplier.toEntity() }.distinct())
            .andThen(localDataSourceProduct.insertAll(products.map { it.toEntity() }))
            .subscribeOn(Schedulers.io())
    }

    override fun addProduct(product: Product): Completable {
        return remoteDataSourceProduct.postProduct(product.toDto())
            .andThen(
                localDataSourceProduct.getMaxId()
                    .map { maxId -> maxId + 1 }
                    .flatMapCompletable { nextId ->
                        val productWithId = product.toEntity().copy(id = nextId)
                        localDataSourceProduct.insertProduct(productWithId).onErrorComplete()
                    }
            )
            .subscribeOn(Schedulers.io())
    }

    override fun updateProduct(product: Product): Completable {
        return remoteDataSourceProduct.updateProduct(product.toDto())
            .andThen(localDataSourceProduct.updateProduct(product.toEntity()).onErrorComplete())
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProductByProductId(productId: Int): Completable {
        return remoteDataSourceProduct.deleteProductById(productId)
            .andThen(localDataSourceProduct.deleteProductById(productId).onErrorComplete())
            .subscribeOn(Schedulers.io())
    }

    override fun searchProducts(query: String): Flowable<List<Product>> {
        return localDataSourceProduct.searchProducts(query)
            .subscribeOn(Schedulers.io())
            .map { products -> products.map { it.toDomain() }.sortedBy { it.id } }
            .toFlowable()
    }

    override fun searchProductBarcode(barcode: String): Maybe<Product> {
        return localDataSourceProduct.searchProductBarcode(barcode)
            .subscribeOn(Schedulers.io())
            .map { it.toDomain() }
    }

    override fun getAllProducts(): Flowable<List<Product>> {
        val local = localDataSourceProduct.getAllProducts()
            .subscribeOn(Schedulers.io())
            .map { products -> products.map { it.toDomain() } }
            .map { it.sortedBy { product -> product.id } }
        val remote = remoteDataSourceProduct.fetchAllProducts()
            .mapToProductWithSupplier()
            .map { it.sortedBy { product -> product.id } }
            .doOnSuccess { productWithSuppliers -> updateDb(productWithSuppliers).subscribe() }
        return Single.concatArrayEager(local, remote)
    }

    override fun getProductById(id: Int): Flowable<Product> {
        val local = localDataSourceProduct.getProductById(id)
            .subscribeOn(Schedulers.io())
            .map { it.toDomain() }
        val remote = remoteDataSourceProduct.fetchProductById(id)
            .mapToProductWithSupplier()
            .doOnSuccess { productWithSuppliers ->
                updateDb(listOf(productWithSuppliers)).subscribe()
            }
        return Maybe.concatArrayEager(local, remote)
    }

    override fun getLowStockProducts(): Flowable<List<Product>> {
        val local = localDataSourceProduct.getLowStockProducts()
            .subscribeOn(Schedulers.io())
            .map { products -> products.map { it.toDomain() } }
        val remote = remoteDataSourceProduct.fetchLowStockProducts()
            .mapToProductWithSupplier()
            .doOnSuccess { productWithSuppliers ->
                updateDb(productWithSuppliers).subscribe()
            }
        return Single.concatArrayEager(local, remote)
            .map { it.sortedBy { product -> product.currentStock } }
    }
}