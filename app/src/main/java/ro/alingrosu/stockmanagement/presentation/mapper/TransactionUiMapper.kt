package ro.alingrosu.stockmanagement.presentation.mapper

import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.presentation.model.TransactionUi

fun Transaction.toUiModel(): TransactionUi = TransactionUi(id, date, type, product.toUiModel(), quantity, notes)

fun List<Transaction>.toUiModel(): List<TransactionUi> = this.map { transaction -> transaction.toUiModel() }

fun TransactionUi.toDomainModel(): Transaction = Transaction(id, date, type, product.toDomainModel(), quantity, notes)