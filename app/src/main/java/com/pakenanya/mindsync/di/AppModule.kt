package com.pakenanya.mindsync.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import androidx.room.Room
import okhttp3.OkHttpClient
import dagger.hilt.InstallIn
import javax.inject.Singleton
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import okhttp3.logging.HttpLoggingInterceptor
import dagger.hilt.components.SingletonComponent
import com.pakenanya.mindsync.BuildConfig.BASE_URL
import retrofit2.converter.gson.GsonConverterFactory
import com.pakenanya.mindsync.data.local.room.UserDao
import com.pakenanya.mindsync.data.local.room.ChatsDao
import com.pakenanya.mindsync.data.local.room.NotesDao
import com.pakenanya.mindsync.data.local.room.DocumentsDao
import com.pakenanya.mindsync.data.local.room.MembershipsDao
import com.pakenanya.mindsync.data.repository.AuthRepository
import com.pakenanya.mindsync.data.repository.ChatsRepository
import com.pakenanya.mindsync.data.repository.NotesRepository
import com.pakenanya.mindsync.data.local.room.MindSyncDatabase
import com.pakenanya.mindsync.data.local.room.OrganizationsDao
import com.pakenanya.mindsync.data.remote.retrofit.UserApiService
import com.pakenanya.mindsync.data.repository.DocumentsRepository
import com.pakenanya.mindsync.data.manager.MindSyncAppPreferences
import com.pakenanya.mindsync.data.remote.retrofit.ChatsApiService
import com.pakenanya.mindsync.data.remote.retrofit.NotesApiService
import com.pakenanya.mindsync.data.repository.MembershipsRepository
import com.pakenanya.mindsync.data.repository.OrganizationsRepository
import com.pakenanya.mindsync.data.remote.retrofit.DocumentsApiService
import com.pakenanya.mindsync.data.remote.retrofit.MembershipsApiService
import com.pakenanya.mindsync.data.remote.retrofit.OrganizationsApiService
import com.pakenanya.mindsync.data.remote.retrofit.WhatsappApiService
import com.pakenanya.mindsync.data.repository.UserRepository
import com.pakenanya.mindsync.data.repository.WhatsappRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideMentalQAppPreferences(context: Context): MindSyncAppPreferences {
        return MindSyncAppPreferences(context)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseApp(context: Context): FirebaseApp =
        FirebaseApp.initializeApp(context)
            ?: throw IllegalStateException("FirebaseApp initialization failed")

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        preferencesManager: MindSyncAppPreferences
    ): AuthRepository {
        return AuthRepository(userDao, preferencesManager)
    }

//    @Provides
//    @Singleton
//    fun provideAuthApiService(): AuthApiService {
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .build()
//
//        return Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .client(client)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(AuthApiService::class.java)
//    }

    @Provides
    @Singleton
    fun provideMindSyncDatabase(application: Application): MindSyncDatabase {
        return Room.databaseBuilder(
            application,
            MindSyncDatabase::class.java,
            "mindsync_database"
        ).fallbackToDestructiveMigration().build()
    }

    // ------------------- User -------------------
    @Provides
    @Singleton
    fun provideUserDao(
        mindSyncDatabase: MindSyncDatabase
    ): UserDao {
        return mindSyncDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideUserApiService(): UserApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApiService: UserApiService,
        userDao: UserDao,
    ): UserRepository {
        return UserRepository(userApiService, userDao)
    }

    // ------------------- Organization -------------------
    @Provides
    @Singleton
    fun provideOrganizationDao(
        mindSyncDatabase: MindSyncDatabase
    ): OrganizationsDao {
        return mindSyncDatabase.organizationsDao()
    }

    @Provides
    @Singleton
    fun provideOrganizationApiService(): OrganizationsApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OrganizationsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOrganizationRepository(
        organizationApiService: OrganizationsApiService,
        organizationDao: OrganizationsDao,
    ): OrganizationsRepository {
        return OrganizationsRepository(organizationApiService, organizationDao)
    }

    // ------------------- Membership -------------------
    @Provides
    @Singleton
    fun provideMembershipDao(
        mindSyncDatabase: MindSyncDatabase
    ): MembershipsDao {
        return mindSyncDatabase.membershipsDao()
    }

    @Provides
    @Singleton
    fun provideMembershipApiService(): MembershipsApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MembershipsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMembershipRepository(
        membershipApiService: MembershipsApiService,
        membershipDao: MembershipsDao,
    ): MembershipsRepository {
        return MembershipsRepository(membershipApiService, membershipDao)
    }

    // ------------------- Notes -------------------
    @Provides
    @Singleton
    fun provideNotesDao(
        mindSyncDatabase: MindSyncDatabase
    ): NotesDao {
        return mindSyncDatabase.notesDao()
    }

    @Provides
    @Singleton
    fun provideNotesApiService(): NotesApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NotesApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotesRepository(
        notesApiService: NotesApiService,
        notesDao: NotesDao,
    ): NotesRepository {
        return NotesRepository(notesApiService, notesDao)
    }

    // ------------------- Document -------------------
    @Provides
    @Singleton
    fun provideDocumentDao(
        mindSyncDatabase: MindSyncDatabase
    ): DocumentsDao {
        return mindSyncDatabase.documentsDao()
    }

    @Provides
    @Singleton
    fun provideDocumentApiService(): DocumentsApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DocumentsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDocumentRepository(
        documentApiService: DocumentsApiService,
        documentDao: DocumentsDao,
    ): DocumentsRepository {
        return DocumentsRepository(documentApiService, documentDao)
    }

    // ------------------- Chat -------------------
    @Provides
    @Singleton
    fun provideChatDao(
        mindSyncDatabase: MindSyncDatabase
    ): ChatsDao {
        return mindSyncDatabase.chatsDao()
    }

    @Provides
    @Singleton
    fun provideChatApiService(): ChatsApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ChatsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(
        chatApiService: ChatsApiService,
        chatDao: ChatsDao,
    ): ChatsRepository {
        return ChatsRepository(chatApiService, chatDao)
    }

    // ------------------- Whatsapp -------------------
    @Provides
    @Singleton
    fun provideWhatsappApiService(): WhatsappApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WhatsappApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideWhatsappRepository(
        whatsappApiService: WhatsappApiService
    ): WhatsappRepository {
        return WhatsappRepository(whatsappApiService)
    }
}