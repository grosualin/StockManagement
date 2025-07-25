package ro.alingrosu.stockmanagement.presentation.model

import java.io.Serializable

data class SupplierUi(
    val id: Int = 0,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String
) : Serializable