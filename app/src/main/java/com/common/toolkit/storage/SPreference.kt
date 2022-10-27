package com.common.toolkit.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import androidx.security.crypto.EncryptedSharedPreferences
import java.io.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Preference 存储 通过键值对保持数据
 * 访问权限: 当前应用 (不公开 sharedPreference)
 * Kotlin 委托模式
 */
class SPreference<T>(private val name: String, private val default: T) :
    ReadWriteProperty<Any?, T> {

    companion object {
        lateinit var preference: SharedPreferences

        fun setContext(context: Context) {
            preference = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        }

        //加密 最低支持 api 23
        fun setContext(context: Context, mainKeyAlias: String) {
            preference = EncryptedSharedPreferences.create(
                "${context.packageName}_encrypted", mainKeyAlias, context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        //删除全部数据
        fun clearPreference() = preference.edit().clear().apply()

        //根据key删除存储数据
        fun clearPreference(key: String) = preference.edit().remove(key).apply()

        //序列化对象
        @Throws(IOException::class)
        fun serialize(name: String, obj: Any) {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val objectOutputStream = ObjectOutputStream(
                byteArrayOutputStream
            )
            objectOutputStream.writeObject(obj)
            val serStr = String(Base64.encode(byteArrayOutputStream.toByteArray(), Base64.DEFAULT))
            objectOutputStream.close()
            byteArrayOutputStream.close()
            preference.edit().putString(name, serStr).apply()
        }

        //反序列化对象
        @Throws(IOException::class)
        fun deserialization(name: String): Any {
            val serStr = preference.getString(name, "")
            val objBytes = Base64.decode(serStr!!.toByteArray(), Base64.DEFAULT)
            val byteArrayInputStream = ByteArrayInputStream(objBytes)
            val objectInputStream = ObjectInputStream(byteArrayInputStream)
            val obj = objectInputStream.readObject()
            objectInputStream.close()
            byteArrayInputStream.close()
            return obj
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        return putPreference(name, value)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getPreference(name, default)
    }

    private fun putPreference(name: String, value: T) = with(preference.edit()) {
        when (value) {
            is Int -> putInt(name, value)
            is Long -> putLong(name, value)
            is Float -> putFloat(name, value)
            is String -> putString(name, value)
            is Boolean -> putBoolean(name, value)
            else -> throw IllegalArgumentException("this type can be save into Preference")
        }.apply()
    }

    private fun getPreference(name: String, default: T): T = with(preference) {
        val res: Any = when (default) {
            is Int -> getInt(name, default)
            is Long -> getLong(name, default)
            is Float -> getFloat(name, default)
            is String -> getString(name, default)!!
            is Boolean -> getBoolean(name, default)
            else -> throw IllegalArgumentException("this type can be get form Preference")
        }
        return res as T
    }

}