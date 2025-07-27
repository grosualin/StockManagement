package ro.alingrosu.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.entity.ProductEntity
import ro.alingrosu.stockmanagement.data.local.entity.ProductWithSupplierEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: ProductEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(products: List<ProductEntity>): Completable

    @Query("SELECT MAX(id) FROM product")
    fun getMaxId(): Single<Int>

    @Update
    fun updateProduct(product: ProductEntity): Completable

    @Delete
    fun deleteProduct(product: ProductEntity): Completable

    @Query("DELETE FROM product WHERE id = :productId")
    fun deleteProductById(productId: Int): Completable

    @Transaction
    @Query(
        """
    SELECT product.*
    FROM product
    JOIN supplier ON product.supplierId = supplier.id
    WHERE product.name LIKE '%' || :query || '%'
       OR product.description LIKE '%' || :query || '%'
       OR supplier.name LIKE '%' || :query || '%'
"""
    )
    fun searchProducts(query: String): Single<List<ProductWithSupplierEntity>>

    @Query("SELECT * FROM product WHERE barcode = :barcode")
    fun searchProductBarcode(barcode: String): Maybe<ProductWithSupplierEntity>

    @Transaction
    @Query("SELECT * FROM product")
    fun getAllProducts(): Single<List<ProductWithSupplierEntity>>

    @Transaction
    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductById(id: Int): Maybe<ProductWithSupplierEntity>

    @Transaction
    @Query("SELECT * FROM product WHERE currentStock < minStock")
    fun getLowStockProducts(): Single<List<ProductWithSupplierEntity>>
}