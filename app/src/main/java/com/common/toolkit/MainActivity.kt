package com.common.toolkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.common.toolkit.storage.SPreference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSPreference()
    }

    private fun testSPreference(){
        SPreference.setContext(applicationContext)

        var string :String by SPreference("string Key", "")

        string = "Test SPreference !!!"
    }
}