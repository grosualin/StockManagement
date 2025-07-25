package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity
import ro.alingrosu.stockmanagement.data.local.entity.TransactionWithProductEntity
import ro.alingrosu.stockmanagement.data.service.dto.TransactionDto
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionType
import ro.alingrosu.stockmanagement.domain.model.TransactionWithProduct

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id, date, TransactionType.fromValue(type), productId, quantity, notes ?: ""
)

fun TransactionEntity.toDto(): TransactionDto = TransactionDto(
    id, date, type, productId, quantity, notes
)

fun TransactionDto.toDomain(): Transaction = Transaction(
    id, date, TransactionType.fromValue(type), productId, quantity, notes ?: ""
)

fun TransactionDto.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type, productId, quantity, notes
)

fun Transaction.toDto(): TransactionDto = TransactionDto(
    id, date, type.value, productId, quantity, notes
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type.value, productId, quantity, notes
)

fun TransactionWithProductEntity.toDomain(): TransactionWithProduct = TransactionWithProduct(
    transaction.toDomain(), product.toDomain()
)