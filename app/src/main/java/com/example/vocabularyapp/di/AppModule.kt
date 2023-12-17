package com.example.vocabularyapp.di

import com.example.vocabularyapp.data.datasource.AuthDataSource
import com.example.vocabularyapp.data.datasource.AuthDataSourceImpl
import com.example.vocabularyapp.data.repository.AuthRepository
import com.example.vocabularyapp.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }


    @Singleton
    @Provides
    fun provideAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideAuthDataSource(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthDataSource {
        return AuthDataSourceImpl(auth, firestore)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(
        authenticator: AuthDataSource
    ): AuthRepository {
        return AuthRepositoryImpl(authenticator)
    }

}
