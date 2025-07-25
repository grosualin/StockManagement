package ro.alingrosu.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity
import ro.alingrosu.stockmanagement.data.local.entity.TransactionWithProductEntity

@Dao
interface TransactionDao {

    @Insert
    fun insertTransaction(transaction: TransactionEntity): Completable

    @Transaction
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(transactions: List<TransactionEntity>): Completable

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Single<List<TransactionWithProductEntity>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Single<List<TransactionWithProductEntity>>

    @Transaction
    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Single<List<TransactionWithProductEntity>>

    @Transaction
    @Query("SELECT * FROM transactions WHERE productId = :productId")
    fun getTransactionsByProductId(productId: Int): Single<List<TransactionWithProductEntity>>
}