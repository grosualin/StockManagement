package ro.alingrosu.stockmanagement.domain.usecase

import io.reactivex.rxjava3.core.Single
import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.model.Transaction
import ro.alingrosu.stockmanagement.domain.repository.ProductRepository
import ro.alingrosu.stockmanagement.domain.repository.TransactionRepository
import javax.inject.Inject

interface DashboardUseCase {
    fun getDashboardData(): Single<Pair<List<Product>, List<Transaction>>>
}

class DashboardUseCaseUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository,
    private val trasactionRepository: TransactionRepository
) : DashboardUseCase {
    override fun getDashboardData(): Single<Pair<List<Product>, List<Transaction>>> {
        return Single.zip(
            productRepository.getLowStockProducts().onErrorReturn { listOf<Product>() },
            trasactionRepository.getRecentTransactions(3).onErrorReturn { listOf<Transaction>() }
        ) { lowStockProducts, recentTransactions ->
            Pair(lowStockProducts, recentTransactions)
        }
    }
}