package ro.alingrosu.stockmanagement.data.service

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ro.alingrosu.stockmanagement.data.service.dto.ProductDto
import java.util.concurrent.TimeUnit

interface ProductService {
    fun postProduct(product: ProductDto): Completable
    fun updateProduct(product: ProductDto): Completable
    fun deleteProductById(productId: Int): Completable
    fun fetchAllProducts(): Single<List<ProductDto>>
    fun fetchProductById(id: Int): Maybe<ProductDto>
    fun fetchLowStockProducts(): Single<List<ProductDto>>
    fun searchProducts(query: String): Single<List<ProductDto>>
}

class ProductMockServiceImpl : ProductService {
    private val mockProducts = mutableListOf(
        ProductDto(1, "Mock Product 1", "Product description 1", 10.0, "Category1", "Barcode1", 1, 50, 5),
        ProductDto(2, "Mock Product 2", "Product description 2", 20.0, "Category2", "Barcode2", 2, 30, 5),
        ProductDto(3, "Mock Product 3", "Product description 3", 15.0, "Category1", "Barcode3", 2, 9, 15),
        ProductDto(4, "Mock Product 4", "Product description 4", 5.0, "Category2", "Barcode4", 1, 10, 5),
        ProductDto(5, "Mock Product 5", "Product description 5", 7.0, "Category3", "Barcode5", 3, 20, 15),
        ProductDto(6, "Mock Product 6", "Product description 6", 43.0, "Category3", "Barcode6", 4, 9, 15),
        ProductDto(7, "Mock Product 7", "Product description 7", 104.0, "Category4", "Barcode7", 2, 9, 35),
        ProductDto(8, "Mock Product 8", "Product description 8", 1577.0, "Category4", "Barcode8", 5, 15, 5),
        ProductDto(9, "Mock Product 9", "Product description 9", 21.0, "Category2", "Barcode9", 4, 30, 5),
        ProductDto(10, "Mock Product 10", "Product description 10", 13.0, "Category5", "Barcode10", 3, 40, 5),
        ProductDto(11, "Mock Product 11", "Product description 11", 33.0, "Category6", "Barcode11", 5, 60, 5),
    )

    override fun postProduct(product: ProductDto): Completable {
        return Completable.fromAction {
            mockProducts.add(product.copy(id = (mockProducts.maxOfOrNull { it.id } ?: 0) + 1))
        }
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun updateProduct(product: ProductDto): Completable {
        return Completable.complete()
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun deleteProductById(productId: Int): Completable {
        return Completable.complete()
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun fetchAllProducts(): Single<List<ProductDto>> {
        return Single.just(mockProducts.toList())
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun fetchProductById(id: Int): Maybe<ProductDto> {
        return Maybe.create { emitter ->
            val product = mockProducts.find { it.id == id }
            if (product != null) {
                emitter.onSuccess(product)
            } else {
                emitter.onComplete()
            }
        }
            .subscribeOn(Schedulers.io())
    }

    override fun fetchLowStockProducts(): Single<List<ProductDto>> {
        return Single.just(
            mockProducts.filter { it.currentStock < it.minStock }
        )
            .delay(2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
    }

    override fun searchProducts(query: String): Single<List<ProductDto>> {
        return Single.just(
            mockProducts.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true) ||
                        it.barcode.contains(query, ignoreCase = true)
            }
        )
            .subscribeOn(Schedulers.io())
    }
}