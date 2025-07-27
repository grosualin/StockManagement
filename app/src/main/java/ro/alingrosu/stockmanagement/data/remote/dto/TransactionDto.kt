package ro.alingrosu.stockmanagement.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("date")
    val date: Long,

    @SerializedName("type")
    val type: String, // "restock" or "sale"

    @SerializedName("product")
    val productId: Int,

    @SerializedName("quantity")
    val quantity: Int,

    @SerializedName("notes")
    val notes: String?
)