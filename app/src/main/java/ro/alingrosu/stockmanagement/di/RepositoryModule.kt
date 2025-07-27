package ro.alingrosu.stockmanagement.di

import dagger.Module
import dagger.Provides
import ro.alingrosu.stockmanagement.data.local.dao.ProductDao
import ro.alingrosu.stockmanagement.data.local.dao.SupplierDao
import ro.alingrosu.stockmanagement.data.local.dao.TransactionDao
import ro.alingrosu.stockmanagement.data.repository.AuthRepositoryImpl
import ro.alingrosu.stockmanagement.data.repository.ProductRepositoryImpl
import ro.alingrosu.stockmanagement.data.repository.SupplierRepositoryImpl
import ro.alingrosu.stockmanagement.data.repository.TransactionRepositoryImpl
import ro.alingrosu.stockmanagement.data.remote.AuthService
import ro.alingrosu.stockmanagement.data.remote.ProductService
import ro.alingrosu.stockmanagement.data.remote.SupplierService
import ro.alingrosu.stockmanagement.data.remote.TransactionService
import ro.alingrosu.stockmanagement.domain.repository.AuthRepository
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import ro.alingrosu.stockmanagement.domain.repository.SupplierRepository
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)

    @Provides
    @Singleton
    fun provideProductRepository(
        dao: ProductDao,
        daoSupplier: SupplierDao,
        api: ProductService,
        apiSupplier: SupplierService
    ): ProductRepository =
        ProductRepositoryImpl(dao, daoSupplier, api, apiSupplier)

    @Provides
    @Singleton
    fun provideSupplierRepository(dao: SupplierDao, api: SupplierService): SupplierRepository =
        SupplierRepositoryImpl(dao, api)

    @Provides
    @Singleton
    fun provideTransactionRepository(
        dao: TransactionDao,
        daoProduct: ProductDao,
        daoSupplier: SupplierDao,
        api: TransactionService,
        apiProduct: ProductService,
        apiSupplier: SupplierService
    ): TransactionRepository =
        TransactionRepositoryImpl(dao, daoProduct, daoSupplier, api, apiProduct, apiSupplier)
}