package ro.alingrosu.stockmanagement.presentation.model

sealed class DashboardListItem {
    data class ProductListItem(val product: ProductUi) : DashboardListItem()
    data class TransactionListItem(val transaction: TransactionUi) : DashboardListItem()
}