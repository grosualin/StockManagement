package ro.alingrosu.stockmanagement.di

import dagger.Module
import dagger.Provides
import ro.alingrosu.stockmanagement.data.remote.AuthMockServiceImpl
import ro.alingrosu.stockmanagement.data.remote.AuthService
import ro.alingrosu.stockmanagement.data.remote.ProductMockServiceImpl
import ro.alingrosu.stockmanagement.data.remote.ProductService
import ro.alingrosu.stockmanagement.data.remote.SupplierMockServiceImpl
import ro.alingrosu.stockmanagement.data.remote.SupplierService
import ro.alingrosu.stockmanagement.data.remote.TransactionMockServiceImpl
import ro.alingrosu.stockmanagement.data.remote.TransactionService

@Module
class ServiceModule {

    @Provides
    fun provideAuthService(): AuthService = AuthMockServiceImpl()

    @Provides
    fun provideProductService(): ProductService = ProductMockServiceImpl()

    @Provides
    fun provideSupplierService(): SupplierService = SupplierMockServiceImpl()

    @Provides
    fun provideTransactionService(): TransactionService = TransactionMockServiceImpl()

}