package org.softwaremaestro.data.common.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.utils.SavedToken
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SavedTokenModule {

    @Singleton
    @Provides
    fun provideSaveToken(): SavedToken {
        return SavedToken()
    }
}