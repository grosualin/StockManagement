package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Flowable
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

interface DashboardUseCase {
    fun getDashboardData(): Flowable<Pair<List<Product>, List<Transaction>>>
}

class DashboardUseCaseUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
    private val transactionRepository: TransactionRepository
) : DashboardUseCase {
    
    override fun getDashboardData(): Flowable<Pair<List<Product>, List<Transaction>>> {
        return Flowable.zip(
            productRepository.getLowStockProducts().onErrorReturn { listOf<Product>() },
            transactionRepository.getRecentTransactions(3).onErrorReturn { listOf<Transaction>() }
        ) { lowStockProducts, recentTransactions ->
            Pair(lowStockProducts, recentTransactions)
        }
    }
}