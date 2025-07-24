package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity
import ro.alingrosu.stockmanagement.data.service.dto.TransactionDto
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionType

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id, date, TransactionType.valueOf(type), productId, quantity, notes ?: ""
)

fun TransactionEntity.toDto(): TransactionDto = TransactionDto(
    id, date, type, productId, quantity, notes
)

fun TransactionDto.toDomain(): Transaction = Transaction(
    id, date, TransactionType.valueOf(type), productId, quantity, notes ?: ""
)

fun TransactionDto.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type, productId, quantity, notes
)

fun Transaction.toDto(): TransactionDto = TransactionDto(
    id, date, type.name, productId, quantity, notes
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type.name, productId, quantity, notes
)