package ro.alingrosu.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.entity.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: ProductEntity): Completable

    @Update
    fun updateProduct(product: ProductEntity): Completable

    @Delete
    fun deleteProduct(product: ProductEntity): Completable

    @Query("DELETE FROM product WHERE id = :productId")
    fun deleteProductByProductId(productId: Int): Completable

    @Query("SELECT * FROM product WHERE name LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Single<List<ProductEntity>>

    @Query("SELECT * FROM product")
    fun getAllProducts(): Single<List<ProductEntity>>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductById(id: Int): Maybe<ProductEntity>

    @Query("SELECT * FROM product WHERE currentStock < minStock")
    fun getLowStockProducts(): Single<List<ProductEntity>>
}