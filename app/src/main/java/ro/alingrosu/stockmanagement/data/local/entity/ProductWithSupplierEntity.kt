package ro.alingrosu.stockmanagement.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProductWithSupplierEntity(
    @Embedded val product: ProductEntity,

    @Relation(
        parentColumn = "supplierId",
        entityColumn = "id"
    )
    val supplier: SupplierEntity
)