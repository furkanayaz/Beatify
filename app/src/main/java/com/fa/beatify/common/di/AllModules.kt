package com.fa.beatify.common.di

import com.fa.beatify.data.local.di.dataLocalModule
import com.fa.beatify.data.remote.di.dataRemoteModule
import com.fa.beatify.domain.local.di.domainLocalModule
import com.fa.beatify.domain.remote.di.domainRemoteModule
import com.fa.beatify.presentation.di.vmModule
import com.fa.beatify.utils.di.utilsModule
import org.koin.core.module.Module
import org.koin.dsl.module

val allModules: Module = module {
    includes(
        dataLocalModule,
        dataRemoteModule,
        domainLocalModule,
        domainRemoteModule,
        vmModule,
        utilsModule
    )
}