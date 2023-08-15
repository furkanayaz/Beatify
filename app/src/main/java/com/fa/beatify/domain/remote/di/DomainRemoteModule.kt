package com.fa.beatify.domain.remote.di

import com.fa.beatify.domain.remote.impl.DeezerDataImpl
import com.fa.beatify.domain.remote.use_cases.AllAlbumsUseCase
import com.fa.beatify.domain.remote.use_cases.AllArtistsUseCase
import com.fa.beatify.domain.remote.use_cases.AllGenresUseCase
import com.fa.beatify.domain.remote.use_cases.AllTracksUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val domainRemoteModule: Module = module {

    single {
        DeezerDataImpl(deezerDataSource = get())
    }

    single {
        AllAlbumsUseCase(deezerDataImpl = get())
    }

    single {
        AllArtistsUseCase(deezerDataImpl = get())
    }

    single {
        AllGenresUseCase(deezerDataImpl = get())
    }

    single {
        AllTracksUseCase(deezerDataImpl = get())
    }

}