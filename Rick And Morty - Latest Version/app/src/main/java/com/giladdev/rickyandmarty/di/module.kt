package com.giladdev.rickyandmarty.di
import android.widget.ListView
import org.koin.dsl.module
import com.giladdev.rickyandmarty.model.CharectersApiService
import com.giladdev.rickyandmarty.viewmodel.ListViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject

val appModule = module {
    single <CharectersApiService> {CharectersApiService()}
}

class CharactersServicesComponent : KoinComponent {
    val service by inject<CharectersApiService>()
}