package ro.alingrosu.stockmanagement.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity

@Dao
interface TransactionDao {

    @Insert
    fun insertTransaction(transaction: TransactionEntity): Completable

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Single<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Single<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY date DESC LIMIT :limit")
    fun getRecentTransactions(limit: Int): Single<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE productId = :productId")
    fun getTransactionsByProductId(productId: Int): Single<List<TransactionEntity>>
}