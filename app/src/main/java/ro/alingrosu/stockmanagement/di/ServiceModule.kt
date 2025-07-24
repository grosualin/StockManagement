package ro.alingrosu.stockmanagement.di

import dagger.Module
import dagger.Provides
import ro.alingrosu.stockmanagement.data.service.AuthMockService
import ro.alingrosu.stockmanagement.data.service.AuthService

@Module
class ServiceModule {

    @Provides
    fun provideAuthService(): AuthService = AuthMockService()
}