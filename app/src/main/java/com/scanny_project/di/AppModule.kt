package com.scanny_project.di

import com.scanny_project.data.*
import com.scanny_project.data.repository.UserRepository
import com.scanny_project.data.SessionManager
import com.scanny_project.data.repository.UserDataSource
import com.scanny_project.data.services.LectureService
import com.scanny_project.data.services.StatsService
import com.scanny_project.data.services.UserQuestionAttemptService
import com.scanny_project.data.services.UserService
import com.scanny_project.data.network.AuthInterceptor
import com.scanny_project.data.services.QuestionService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): AuthInterceptor {
        return AuthInterceptor(sessionManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl("http://10.0.2.2:8080/")
            .baseUrl("https://scanny-803057483420.europe-west3.run.app")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideLectureService(retrofit: Retrofit): LectureService {
        return retrofit.create(LectureService::class.java)
    }
    @Provides
    @Singleton
    fun provideQuestionService(retrofit: Retrofit): QuestionService {
        return retrofit.create(QuestionService::class.java)
    }

    @Provides
    @Singleton
    fun provideStatsService(retrofit: Retrofit): StatsService {
        return retrofit.create(StatsService::class.java)
    }


    @Provides
    @Singleton
    fun provideLoginDataSource(userService: UserService): UserDataSource {
        return UserDataSource(userService)
    }

    @Provides
    @Singleton
    fun provideLoginRepository(
        dataSource: UserDataSource,
        sessionManager: SessionManager
    ): UserRepository {
        return UserRepository(dataSource, sessionManager)
    }

    @Provides
    @Singleton
    fun provideAttemptsService(retrofit: Retrofit): UserQuestionAttemptService {
        return retrofit.create(UserQuestionAttemptService::class.java)
    }
}
