package ro.alingrosu.stockmanagement.di

import dagger.Component
import ro.alingrosu.stockmanagement.presentation.StockApplication
import ro.alingrosu.stockmanagement.presentation.ui.auth.login.LoginViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.dashboard.DashboardViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.product.detail.ProductDetailViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.product.list.ProductListViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.stock.StockManagementViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.supplier.detail.SupplierDetailViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.supplier.list.SupplierListViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.transaction.TransactionListViewModel
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        UseCasesModule::class,
        RepositoryModule::class,
        DatabaseModule::class,
        ServiceModule::class
    ]
)
interface AppComponent {
    fun inject(app: StockApplication)

    val loginViewModel: LoginViewModel

    val dashboardViewModel: DashboardViewModel

    val productDetailViewModel: ProductDetailViewModel
    val productListViewModel: ProductListViewModel

    val supplierDetailViewModel: SupplierDetailViewModel
    val supplierListViewModel: SupplierListViewModel

    val stockManagementViewModel: StockManagementViewModel

    val transactionListViewModel: TransactionListViewModel
}