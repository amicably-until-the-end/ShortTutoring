package org.softwaremaestro.data.di

import android.content.Context
import dagger.Component
import dagger.hilt.android.qualifiers.ApplicationContext

@ActivityScope
@Component(dependencies = [ApplicationContext::class])
interface ActivityComponent {
    fun getActivityContext(): Context
}