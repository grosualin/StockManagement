package ro.alingrosu.stockmanagement.domain.model

enum class TransactionType(val value: String) {
    RESTOCK("restock"),
    SALE("sale");

    companion object {
        fun fromValue(value: String): TransactionType = entries.first { it.value == value }
    }
}