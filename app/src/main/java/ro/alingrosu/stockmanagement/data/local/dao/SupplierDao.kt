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
import ro.alingrosu.stockmanagement.data.local.entity.SupplierEntity
import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity

@Dao
interface SupplierDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertSupplier(supplier: SupplierEntity): Completable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(suppliers: List<SupplierEntity>): Completable

    @Update
    fun updateSupplier(supplier: SupplierEntity): Completable

    @Delete
    fun deleteSupplier(supplier: SupplierEntity): Completable

    @Query("DELETE FROM supplier WHERE id = :supplierId")
    fun deleteSupplierById(supplierId: Int): Completable

    @Query("SELECT * FROM supplier WHERE name LIKE '%' || :query || '%'")
    fun searchSuppliers(query: String): Single<List<SupplierEntity>>

    @Query("SELECT * FROM supplier")
    fun getAllSuppliers(): Single<List<SupplierEntity>>

    @Query("SELECT * FROM supplier WHERE id = :id")
    fun getSupplierById(id: Int): Maybe<SupplierEntity>
}