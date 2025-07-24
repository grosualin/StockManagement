package ro.alingrosu.stockmanagement.domain.model

data class Supplier(
    val id: Int = 0,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String
)