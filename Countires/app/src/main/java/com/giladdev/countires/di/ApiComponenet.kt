package com.giladdev.countires.di

import com.giladdev.countires.Model.CountriesService
import com.giladdev.countires.ViewModel.ListViewModel
import dagger.Component

@Component(modules=[ApiModule::class])

interface ApiComponenet{
    fun inject(service : CountriesService)

    fun inject(viewModel: ListViewModel)
}