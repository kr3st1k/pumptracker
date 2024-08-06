package com.kr3st1k.pumptracker.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ManagerModules {
    val bgModule = module {
        singleOf(::BgManager)
    }

    val internetModule = module {
        singleOf(::InternetManager)
    }

    val loginModule = module {
        singleOf(::LoginManager)
    }
}