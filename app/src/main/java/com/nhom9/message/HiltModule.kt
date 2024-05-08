package com.nhom9.message

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context):
            MApplication {
        return app as MApplication
    }

    @Provides
    fun provideAuthentication(): FirebaseAuth = Firebase.auth

    @Provides
    fun provideStorage(): FirebaseStorage = Firebase.storage

    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore
}