package com.fa.beatify.domain.remote.di

import com.fa.beatify.domain.remote.use_cases.AllAlbumsUseCase
import com.fa.beatify.domain.remote.use_cases.AllArtistsUseCase
import com.fa.beatify.domain.remote.use_cases.AllGenresUseCase
import com.fa.beatify.domain.remote.use_cases.AllTracksUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val domainRemoteModule: Module = module {

    single {
        AllAlbumsUseCase(get())
    }

    single {
        AllArtistsUseCase(get())
    }

    single {
        AllGenresUseCase(get())
    }

    single {
        AllTracksUseCase(get())
    }

}