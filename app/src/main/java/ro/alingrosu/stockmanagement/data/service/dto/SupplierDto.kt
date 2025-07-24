package ro.alingrosu.stockmanagement.data.service.dto

import com.google.gson.annotations.SerializedName

data class SupplierDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("contactPerson")
    val contactPerson: String,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("address")
    val address: String
)