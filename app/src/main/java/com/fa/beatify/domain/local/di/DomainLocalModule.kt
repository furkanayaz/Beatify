package com.fa.beatify.domain.local.di

import com.fa.beatify.domain.local.use_cases.AllLikesUseCase
import com.fa.beatify.domain.local.use_cases.DeleteLikeUseCase
import com.fa.beatify.domain.local.use_cases.InsertLikeUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val domainLocalModule: Module = module {

    single {
        AllLikesUseCase(likeDataSource = get())
    }

    single {
        DeleteLikeUseCase(likeDataSource = get())
    }

    single {
        InsertLikeUseCase(likeDataSource = get())
    }

}