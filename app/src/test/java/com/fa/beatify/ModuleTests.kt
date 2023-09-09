package com.fa.beatify

import com.fa.beatify.common.di.allModules
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.verify.verifyAll

@OptIn(KoinInternalApi::class)
class ModuleTests: KoinTest {
    private var moduleList: MutableList<Module>? = null

    @Before
    fun setup() {
        moduleList = allModules.includedModules
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkAllModules() {
        moduleList?.verifyAll()
    }

    @After
    fun teardown() { moduleList?.clear() }

}