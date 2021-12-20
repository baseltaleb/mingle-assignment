package tech.mingle.basel.assignment.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.mingle.basel.assignment.api.AssignmentService
import tech.mingle.basel.assignment.data.Constants
import tech.mingle.basel.assignment.data.dao.AssignmentDao
import tech.mingle.basel.assignment.data.database.AssignmentDatabase
import tech.mingle.basel.assignment.data.preferences.PrefsStore
import tech.mingle.basel.assignment.data.preferences.PrefsStoreImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideAssignmentService(): AssignmentService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AssignmentService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AssignmentDatabase {
        return Room
            .databaseBuilder(app, AssignmentDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideAssignmentDao(db: AssignmentDatabase): AssignmentDao = db.assignmentDao()

    @Singleton
    @Provides
    fun providePreferencesDataStore(): PrefsStore = PrefsStoreImpl()

}