package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.ProductEntity
import ro.alingrosu.stockmanagement.data.local.entity.SupplierEntity
import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity
import ro.alingrosu.stockmanagement.data.local.entity.TransactionWithProductEntity
import ro.alingrosu.stockmanagement.data.remote.dto.ProductDto
import ro.alingrosu.stockmanagement.data.remote.dto.SupplierDto
import ro.alingrosu.stockmanagement.data.remote.dto.TransactionDto
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionType

fun TransactionEntity.toDomain(productEntity: ProductEntity, supplierEntity: SupplierEntity): Transaction = Transaction(
    id, date, TransactionType.fromValue(type), productEntity.toDomain(supplierEntity), quantity, notes ?: ""
)

fun TransactionEntity.toDto(): TransactionDto = TransactionDto(
    id, date, type, productId, quantity, notes
)

fun TransactionDto.toDomain(productDto: ProductDto, supplierDto: SupplierDto): Transaction = Transaction(
    id, date, TransactionType.fromValue(type), productDto.toDomain(supplierDto), quantity, notes ?: ""
)

fun TransactionDto.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type, productId, quantity, notes
)

fun Transaction.toDto(): TransactionDto = TransactionDto(
    id, date, type.value, product.id, quantity, notes
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type.value, product.id, quantity, notes
)

fun TransactionWithProductEntity.toDomain(): Transaction = transaction.toDomain(product.product, product.supplier)