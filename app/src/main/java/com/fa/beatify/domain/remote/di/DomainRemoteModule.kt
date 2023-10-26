package com.fa.beatify.domain.remote.di

import com.fa.beatify.domain.remote.impl.DeezerDataSourceImpl
import com.fa.beatify.domain.remote.use_cases.AllAlbumsUseCase
import com.fa.beatify.domain.remote.use_cases.AllArtistsUseCase
import com.fa.beatify.domain.remote.use_cases.AllGenresUseCase
import com.fa.beatify.domain.remote.use_cases.AllTracksUseCase
import org.koin.core.module.Module
import org.koin.dsl.module

val domainRemoteModule: Module = module {

    single {
        DeezerDataSourceImpl(client = get())
    }

    single {
        AllAlbumsUseCase(deezerDataSourceImpl = get())
    }

    single {
        AllArtistsUseCase(deezerDataSourceImpl = get())
    }

    single {
        AllGenresUseCase(deezerDataSourceImpl = get())
    }

    single {
        AllTracksUseCase(deezerDataSourceImpl = get())
    }

}