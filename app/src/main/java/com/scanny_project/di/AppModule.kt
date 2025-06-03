package com.scanny_project.di

import com.scanny_project.data.*
import com.scanny_project.data.repository.UserRepository
import com.scanny_project.data.SessionManager
import com.scanny_project.data.api.LectureApi
import com.scanny_project.data.api.QuestionApi
import com.scanny_project.data.api.UserApi
import com.scanny_project.data.network.AuthInterceptor
import com.scanny_project.data.repository.LectureRepository
import com.scanny_project.data.repository.QuestionRepository
import com.scanny_project.data.repository.impl.LectureRepositoryImpl
import com.scanny_project.data.repository.impl.QuestionRepositoryImpl
import com.scanny_project.data.repository.impl.UserRepositoryImpl
import com.scanny_project.data.api.StatsApi
import com.scanny_project.data.api.UserQuestionAttemptApi
import com.scanny_project.data.repository.AttemptsRepository
import com.scanny_project.data.repository.StatsRepository
import com.scanny_project.data.repository.impl.AttemptsRepositoryImpl
import com.scanny_project.data.repository.impl.StatsRepositoryImpl
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
            .baseUrl("http://10.0.2.2:8080/")
//            .baseUrl("https://scanny-803057483420.europe-west3.run.app")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideLectureApi(retrofit: Retrofit): LectureApi {
        return retrofit.create(LectureApi::class.java)
    }
    @Provides
    @Singleton
    fun provideLectureRepository(api: LectureApi): LectureRepository =
        LectureRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideQuestionApi(retrofit: Retrofit): QuestionApi {
        return retrofit.create(QuestionApi::class.java)
    }
    @Provides
    @Singleton
    fun provideQuestionRepository(api: QuestionApi): QuestionRepository =
        QuestionRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideStatsService(retrofit: Retrofit): StatsApi {
        return retrofit.create(StatsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStatsRepository(api: StatsApi): StatsRepository =
        StatsRepositoryImpl(api)


    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: UserApi,
        sessionManager: SessionManager
    ): UserRepository {
        return UserRepositoryImpl(userApi, sessionManager)
    }

    @Provides
    @Singleton
    fun provideAttemptsService(retrofit: Retrofit): UserQuestionAttemptApi {
        return retrofit.create(UserQuestionAttemptApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttemptsRepository(api: UserQuestionAttemptApi): AttemptsRepository =
        AttemptsRepositoryImpl(api)
}
