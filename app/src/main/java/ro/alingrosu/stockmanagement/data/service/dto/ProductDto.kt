package ro.alingrosu.stockmanagement.data.service.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(

    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("category")
    val category: String,

    @SerializedName("barcode")
    val barcode: String,

    @SerializedName("supplierId")
    val supplierId: Int,

    @SerializedName("currentStock")
    val currentStock: Int,
    
    @SerializedName("minStock")
    val minStock: Int
)