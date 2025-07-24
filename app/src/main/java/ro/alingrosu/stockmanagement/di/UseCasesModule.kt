package ro.alingrosu.stockmanagement.di

import dagger.Module
import dagger.Provides
import ro.alingrosu.stockmanagement.domain.repository.AuthRepository
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import ro.alingrosu.stockmanagement.domain.repository.SupplierRepository
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import ro.alingrosu.stockmanagement.domain.usecase.AuthUseCase
import ro.alingrosu.stockmanagement.domain.usecase.AuthUseCaseImpl
import ro.alingrosu.stockmanagement.domain.usecase.DashboardUseCase
import ro.alingrosu.stockmanagement.domain.usecase.DashboardUseCaseUseCaseImpl
import ro.alingrosu.stockmanagement.domain.usecase.ProductUseCase
import ro.alingrosu.stockmanagement.domain.usecase.ProductUseCaseImpl
import ro.alingrosu.stockmanagement.domain.usecase.StockManagementUseCase
import ro.alingrosu.stockmanagement.domain.usecase.StockManagementUseCaseImpl
import ro.alingrosu.stockmanagement.domain.usecase.SupplierUseCase
import ro.alingrosu.stockmanagement.domain.usecase.SupplierUseCaseImpl
import ro.alingrosu.stockmanagement.domain.usecase.TransactionUseCase
import ro.alingrosu.stockmanagement.domain.usecase.TransactionUseCaseImpl
import javax.inject.Singleton

@Module
class UseCasesModule {

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(authRepository: AuthRepository): AuthUseCase {
        return AuthUseCaseImpl(authRepository)
    }

    @Provides
    @Singleton
    fun provideProductUseCases(productRepository: ProductRepository): ProductUseCase {
        return ProductUseCaseImpl(productRepository)
    }

    @Provides
    @Singleton
    fun provideSupplierUseCases(supplierRepository: SupplierRepository): SupplierUseCase {
        return SupplierUseCaseImpl(supplierRepository)
    }

    @Provides
    @Singleton
    fun provideTransactionUseCases(transactionRepository: TransactionRepository): TransactionUseCase {
        return TransactionUseCaseImpl(transactionRepository)
    }

    @Provides
    @Singleton
    fun provideStockManagementUseCases(productRepository: ProductRepository): StockManagementUseCase {
        return StockManagementUseCaseImpl(productRepository)
    }

    @Provides
    @Singleton
    fun provideDashboardUseCases(productRepository: ProductRepository, transactionRepository: TransactionRepository): DashboardUseCase {
        return DashboardUseCaseUseCaseImpl(productRepository, transactionRepository)
    }
}