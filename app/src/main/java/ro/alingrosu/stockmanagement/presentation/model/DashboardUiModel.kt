package ro.alingrosu.stockmanagement.presentation.model

data class DashboardUiModel(
    val lowStockItems: List<ProductUi>,
    val recentTransactions: List<TransactionUi>
)