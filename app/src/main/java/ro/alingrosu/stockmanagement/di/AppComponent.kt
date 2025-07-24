package ro.alingrosu.stockmanagement.di

import dagger.Component
import ro.alingrosu.stockmanagement.presentation.StockApplication
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
}