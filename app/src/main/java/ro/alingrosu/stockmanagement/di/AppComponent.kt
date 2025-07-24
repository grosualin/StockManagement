package ro.alingrosu.stockmanagement.di

import dagger.Component
import ro.alingrosu.stockmanagement.presentation.StockApplication
import ro.alingrosu.stockmanagement.presentation.ui.auth.login.LoginViewModel
import ro.alingrosu.stockmanagement.presentation.ui.main.dashboard.DashboardViewModel
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
}