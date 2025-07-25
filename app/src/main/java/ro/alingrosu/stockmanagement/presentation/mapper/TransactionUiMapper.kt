package ro.alingrosu.stockmanagement.presentation.mapper

import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.model.TransactionWithProduct
import ro.alingrosu.stockmanagement.presentation.model.TransactionUi


fun TransactionWithProduct.toUiModel(): TransactionUi =
    TransactionUi(
        transaction.id,
        transaction.date,
        transaction.type,
        product.toUiModel(),
        transaction.quantity,
        transaction.notes
    )

fun List<TransactionWithProduct>.toUiModel(): List<TransactionUi> = map { it.toUiModel() }

fun TransactionUi.toTransaction(): Transaction = Transaction(id, date, type, product.id, quantity, notes)

fun TransactionUi.toDomainModel(): TransactionWithProduct =
    TransactionWithProduct(this.toTransaction(), product.toDomainModel())