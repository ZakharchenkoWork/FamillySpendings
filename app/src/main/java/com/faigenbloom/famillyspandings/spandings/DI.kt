package com.faigenbloom.famillyspandings.spandings

import com.faigenbloom.famillyspandings.datasources.BaseDataSource
import com.faigenbloom.famillyspandings.datasources.MockDataSource
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val spendingsPageModule = module {
    viewModelOf(::SpendingsPageViewModel)
    singleOf(::SpendingsPageRepository)
    singleOf(::MockDataSource) bind BaseDataSource::class
}
