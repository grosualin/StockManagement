package ro.alingrosu.stockmanagement.data.mapper

import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionType

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id, date, TransactionType.valueOf(type), productId, quantity, notes ?: ""
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id, date, type.name, productId, quantity, notes
)