package org.softwaremaestro.data.review

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.softwaremaestro.data.common.module.NetworkModule
import org.softwaremaestro.data.review.remote.ReviewApi
import org.softwaremaestro.domain.review.ReviewRepository
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class ReviewModule {

    @Singleton
    @Provides
    fun provideReviewCreateApi(retrofit: Retrofit): ReviewApi {
        return retrofit.create(ReviewApi::class.java)
    }

    @Singleton
    @Provides
    fun provideReviewCreateRepository(reviewApi: ReviewApi): ReviewRepository {
        return ReviewRepositoryImpl(reviewApi)
    }
}