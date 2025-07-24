package ro.alingrosu.stockmanagement.presentation

import android.app.Application
import android.content.Context
import ro.alingrosu.stockmanagement.di.AppComponent
import ro.alingrosu.stockmanagement.di.AppModule
import ro.alingrosu.stockmanagement.di.DaggerAppComponent
import ro.alingrosu.stockmanagement.di.DatabaseModule
import ro.alingrosu.stockmanagement.di.RepositoryModule
import ro.alingrosu.stockmanagement.di.UseCasesModule

class StockApplication : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .databaseModule(DatabaseModule())
            .repositoryModule(RepositoryModule())
            .useCasesModule(UseCasesModule())
            .build()
        appComponent.inject(this)
    }

    companion object {
        fun getAppComponent(context: Context): AppComponent {
            return (context.applicationContext as StockApplication).appComponent
        }
    }
}