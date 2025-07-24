package ro.alingrosu.stockmanagement.presentation.model

import ro.alingrosu.stockmanagement.domain.model.Product
import ro.alingrosu.stockmanagement.domain.model.Transaction

data class DashboardUiModel(
    val lowStockItems: List<Product>,
    val recentTransactions: List<Transaction>
)