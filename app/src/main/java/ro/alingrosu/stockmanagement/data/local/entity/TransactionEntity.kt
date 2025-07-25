package ro.alingrosu.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("productId")]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val date: Long,
    val type: String, // "restock" or "sale"
    val productId: Int,
    val quantity: Int,
    val notes: String?
)