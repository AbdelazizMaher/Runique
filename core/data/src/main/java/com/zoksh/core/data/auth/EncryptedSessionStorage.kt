package com.zoksh.core.data.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.zoksh.core.domain.AuthInfo
import com.zoksh.core.domain.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class EncryptedSessionStorage(
    private val sharedPreferences: SharedPreferences,
) : SessionStorage {
    override suspend fun get(): AuthInfo? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(authInfo: AuthInfo?) {
        withContext(Dispatchers.IO) {
            sharedPreferences.edit {
                if (authInfo == null) {
                    remove(KEY_AUTH_INFO)
                } else {
                    val json = Json.encodeToString(authInfo.toAuthInfoSerializable())
                    putString(KEY_AUTH_INFO, json)
                }
            }
        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}