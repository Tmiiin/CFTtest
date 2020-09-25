package com.example.cfttest

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BankActivity: AppCompatActivity() {

    val TAG = "BankFragment"
    var APP_PREFERENCES = "bankData"
    lateinit var sPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        sPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        CoroutineScope(IO).launch{
            val connection = Request().getConnection()
            withContext(Main){supportFragmentManager.beginTransaction()  //made a transaction to general page
            .replace(R.id.details_container, ListFragment(sPreferences,applicationContext, connection)).commit()}
        }
    }

}