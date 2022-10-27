package com.common.toolkit

import android.app.Application
import androidx.security.crypto.MasterKeys
import com.common.toolkit.storage.SPreference

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SPreference.setContext(this)// , MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC))
    }
}