package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.ProductEntity
import ro.alingrosu.stockmanagement.domain.model.Product

fun ProductEntity.toDomain(): Product = Product(
    id, name, description, price, category, barcode, supplierId, currentStock, minStock
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id, name, description, price, category, barcode, supplierId, currentStock, minStock
)