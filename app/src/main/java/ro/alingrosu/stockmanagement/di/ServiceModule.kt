package ro.alingrosu.stockmanagement.di

import dagger.Module
import dagger.Provides
import ro.alingrosu.stockmanagement.data.service.AuthMockServiceImpl
import ro.alingrosu.stockmanagement.data.service.AuthService
import ro.alingrosu.stockmanagement.data.service.ProductMockServiceImpl
import ro.alingrosu.stockmanagement.data.service.ProductService
import ro.alingrosu.stockmanagement.data.service.SupplierMockServiceImpl
import ro.alingrosu.stockmanagement.data.service.SupplierService
import ro.alingrosu.stockmanagement.data.service.TransactionMockServiceImpl
import ro.alingrosu.stockmanagement.data.service.TransactionService

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