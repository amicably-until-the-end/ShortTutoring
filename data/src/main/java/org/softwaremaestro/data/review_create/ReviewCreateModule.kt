package org.softwaremaestro.data.review_create

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.review_create.remote.ReviewCreateApi
import org.softwaremaestro.domain.review_create.ReviewCreateRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ReviewCreateModule {

    @Singleton
    @Provides
    fun provideReviewCreateApi(retrofit: Retrofit): ReviewCreateApi {
        return retrofit.create(ReviewCreateApi::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewCreateRepository(reviewCreateApi: ReviewCreateApi): ReviewCreateRepository {
        return ReviewCreateRepositoryImpl(reviewCreateApi)
    }
}