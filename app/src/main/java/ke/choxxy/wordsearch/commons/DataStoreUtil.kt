package ke.choxxy.wordsearch.commons

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import ke.choxxy.wordsearch.data.entity.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@ExperimentalSerializationApi
class DataStoreUtil
@Inject
constructor(
    private val dataStore: DataStore<Preferences>,
    private val security: SecurityUtil
) {
    private val securityKeyAlias = "data-store"
    private val bytesToStringSeparator = "|"
    private val json = Json { encodeDefaults = true }
    private val data = stringPreferencesKey("data")
    private val secureData = stringPreferencesKey("secured_data")
    private val playerInfo = stringPreferencesKey("player_info")

    fun getData() = dataStore.data
        .map { preferences ->
            preferences[data].orEmpty()
        }

    suspend fun setData(value: String) {
        dataStore.edit {
            it[data] = value
        }
    }

    fun getSecuredData() = dataStore.data
        .secureMap<String> { preferences ->
            preferences[secureData].orEmpty()
        }

    suspend fun setSecuredData(value: String) {
        dataStore.secureEdit(value) { prefs, encryptedValue ->
            prefs[secureData] = encryptedValue
        }
    }

    suspend fun hasKey(key: Preferences.Key<*>) = dataStore.edit { it.contains(key) }

    suspend fun clearDataStore() {
        dataStore.edit {
            it.clear()
        }
    }

    private inline fun <reified T> Flow<Preferences>.secureMap(crossinline fetchValue: (value: Preferences) -> String): Flow<T> {
        return map { preferences ->
            val decryptedValue = security.decryptData(
                securityKeyAlias,
                fetchValue(preferences).split(bytesToStringSeparator).map { it.toByte() }.toByteArray()
            )
            json.decodeFromString(decryptedValue)
        }
    }

    private suspend inline fun <reified T> DataStore<Preferences>.secureEdit(
        value: T,
        crossinline editStore: (MutablePreferences, String) -> Unit
    ) {
        edit {
            val encryptedValue = security.encryptData(securityKeyAlias, Json.encodeToString(value))
            editStore.invoke(it, encryptedValue.joinToString(bytesToStringSeparator))
        }
    }

    suspend fun setPlayerInfo(value: Player) {
        dataStore.secureEdit(value) { prefs, encryptedValue ->
            prefs[playerInfo] = encryptedValue
        }
    }

    fun getPlayerInfo() = dataStore.data
        .secureMap<Player> { prefs ->
            prefs[playerInfo].orEmpty()
        }
}
