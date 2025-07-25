package ro.alingrosu.stockmanagement.domain.model

data class TransactionWithProduct(
    val transaction: Transaction,
    val product: Product
)