package ro.alingrosu.stockmanagement.domain.model

data class Transaction(
    val id: Int = 0,
    val date: Long,
    val type: TransactionType,
    val product: Product,
    val quantity: Int,
    val notes: String
)