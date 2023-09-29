package com.example.to_do.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.to_do.data.models.Priority
import com.example.to_do.util.Constants.PREFERENCE_KEY_SORT
import com.example.to_do.util.Constants.PREFERENCE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferenceKey{
        val sortKey = stringPreferencesKey(name = PREFERENCE_KEY_SORT)
        val sortKey2 = stringPreferencesKey(name = "sort_state2")

    }
    private val dataStore = context.dataStore

    suspend fun persistSortState(priority: Priority){
        dataStore.edit {preference ->
            preference[PreferenceKey.sortKey] = priority.name

        }
    }

    val readSortState: Flow<String> = dataStore.data.catch {exception ->
        if (exception is IOException){
            emit(emptyPreferences())
        }else{
            throw  exception
        }
    }.map { preferences -> //Preferences tipinde saklanan verileri stringe çeviriyoruz
        val sortState = preferences[PreferenceKey.sortKey] ?: Priority.NONE.name
        sortState//will be emitted
    }
}