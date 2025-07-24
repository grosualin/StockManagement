package ro.alingrosu.stockmanagement.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "supplier")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val contactPerson: String,
    val phone: String,
    val email: String,
    val address: String
)