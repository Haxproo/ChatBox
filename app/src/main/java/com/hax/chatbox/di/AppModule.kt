package com.hax.chatbox.di

import com.hax.chatbox.data.repository.AuthRepository
import com.hax.chatbox.ui.viewmodel.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { AuthRepository() }
    
    // ViewModels
    viewModel { AuthViewModel(get()) }
}