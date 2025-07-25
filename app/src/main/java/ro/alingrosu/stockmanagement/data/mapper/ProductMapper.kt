package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.ProductEntity
import ro.alingrosu.stockmanagement.data.local.entity.ProductWithSupplierEntity
import ro.alingrosu.stockmanagement.data.local.entity.SupplierEntity
import ro.alingrosu.stockmanagement.data.service.dto.ProductDto
import ro.alingrosu.stockmanagement.data.service.dto.SupplierDto
import ro.alingrosu.stockmanagement.domain.model.Product

fun ProductEntity.toDomain(supplierEntity: SupplierEntity): Product = Product(
    id, name, description, price, category, barcode, supplierEntity.toDomain(), currentStock, minStock
)

fun ProductEntity.toDto(): ProductDto = ProductDto(
    id, name, description, price, category, barcode, supplierId, currentStock, minStock
)

fun ProductDto.toDomain(supplierDto: SupplierDto): Product = Product(
    id, name, description, price, category, barcode, supplierDto.toDomain(), currentStock, minStock
)

fun ProductDto.toEntity(): ProductEntity = ProductEntity(
    id, name, description, price, category, barcode, supplierId, currentStock, minStock
)

fun Product.toDto(): ProductDto = ProductDto(
    id, name, description, price, category, barcode, supplier.id, currentStock, minStock
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    id, name, description, price, category, barcode, supplier.id, currentStock, minStock
)

fun ProductWithSupplierEntity.toDomain(): Product = product.toDomain(supplier)