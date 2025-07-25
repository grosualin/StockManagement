package ro.alingrosu.stockmanagement.domain.model

data class Transaction(
    val id: Int = 0,
    val date: Long,
    val type: TransactionType,
    val productId: Int,
    val quantity: Int,
    val notes: String
)

enum class TransactionType(val value: String) {
    RESTOCK("restock"),
    SALE("sale");

    companion object {
        fun fromValue(value: String): TransactionType = entries.first { it.value == value }
    }
}