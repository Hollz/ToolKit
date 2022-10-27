package com.common.toolkit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.common.toolkit.storage.SPreference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testSPreference()
    }

    private fun testSPreference() {
        var string: String by SPreference("string Key", "")
        string = "Test SPreference !!!"


        var user = User("JOU", 10)

        SPreference.serialize("user", user)

        val user2 = SPreference.deserialization("user") as User

        Log.e("TAG", "${user2.name} ==== ${user2.age}")
        findViewById<TextView>(R.id.text).setOnClickListener {
            SPreference.clearPreference()
        }
    }
}