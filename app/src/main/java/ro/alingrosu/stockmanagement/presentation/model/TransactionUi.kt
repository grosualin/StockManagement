package ro.alingrosu.stockmanagement.presentation.model

import ro.alingrosu.stockmanagement.domain.model.TransactionType
import java.io.Serializable

data class TransactionUi(
    val id: Int = 0,
    val date: Long,
    val type: TransactionType,
    val product: ProductUi,
    val quantity: Int,
    val notes: String
) : Serializable