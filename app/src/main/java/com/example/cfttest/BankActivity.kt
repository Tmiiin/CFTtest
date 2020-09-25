package com.example.cfttest

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class BankActivity: AppCompatActivity() {

    val TAG = "BankFragment"
    val APP_PREFERENCES = "bankData"
    var APP_PREFERENCES_DATE: String = "lastReload"
    lateinit var sPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        sPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        getDate()
        CoroutineScope(IO).launch{
            val connection = Request().getConnection()
            withContext(Main){supportFragmentManager.beginTransaction()  //made a transaction to general page
                .replace(R.id.details_container, ListFragment(sPreferences,applicationContext)).commit()}
        }
    }

    fun getDate(){
        if(!sPreferences.contains(APP_PREFERENCES_DATE))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                sPreferences.edit().putString(APP_PREFERENCES_DATE, LocalDate.now().toString()).apply()
            }
    }

}