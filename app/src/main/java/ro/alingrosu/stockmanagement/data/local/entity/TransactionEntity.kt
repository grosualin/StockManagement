package ro.alingrosu.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val type: String, // "restock" or "sale"
    val productId: Int,
    val quantity: Int,
    val notes: String?
)