package ro.alingrosu.stockmanagement.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ro.alingrosu.stockmanagement.data.local.StockDatabase
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): StockDatabase =
        Room.databaseBuilder(context, StockDatabase::class.java, "stock.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideProductDao(db: StockDatabase): ProductDao = db.productDao()

    @Provides
    fun provideSupplierDao(db: StockDatabase): SupplierDao = db.supplierDao()

    @Provides
    fun provideTransactionDao(db: StockDatabase): TransactionDao = db.transactionDao()
}