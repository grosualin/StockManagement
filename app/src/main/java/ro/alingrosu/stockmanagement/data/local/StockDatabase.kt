package ro.alingrosu.stockmanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.local.entity.ProductEntity
import ro.alingrosu.stockmanagement.data.local.entity.SupplierEntity
import ro.alingrosu.stockmanagement.data.local.entity.TransactionEntity

@Database(entities = [ProductEntity::class, SupplierEntity::class, TransactionEntity::class], version = 2)
abstract class StockDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun supplierDao(): SupplierDao
    abstract fun transactionDao(): TransactionDao
}