package kz.kamran.todoapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kz.kamran.todoapplication.data.remote.provider.UserProvider
import kz.kamran.todoapplication.data.remote.provider.UserProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {
    @Singleton
    @Binds
    abstract fun provideUserProvider(userProviderImpl: UserProviderImpl): UserProvider
}