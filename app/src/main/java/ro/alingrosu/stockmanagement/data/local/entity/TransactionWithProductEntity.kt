package ro.alingrosu.stockmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithProductEntity(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id",
        entity = ProductEntity::class
    )
    val product: ProductWithSupplierEntity
)