package com.example.cfttest

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import com.beust.klaxon.*
import com.example.cfttest.view.CurrencyDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection

class BankPresenter( //for operations with details
    private var mDetailsActivity: ListFragment,
    var sPreferences: SharedPreferences,
    private var mContext: Context,
    val connection: HttpURLConnection
) : Fragment() {

    val jsonObjectValute: String = "Valute"
    val TAG = "RatesPresenter"
    var details: JsonObject? = null

    suspend fun downloadBankDetails(): MutableList<CurrencyDetails>{
        if(details.isNullOrEmpty()) {
            if (isValuteSerialize())
                getSavedDetails()
            else {
                Log.i(TAG, "Started download new exchange rates")
                val json = BufferedReader(InputStreamReader(connection.inputStream))
                val string = StringBuilder()
                var stringNow = json.readLine()
                while (stringNow != null) {
                    string.append(stringNow)
                    stringNow = json.readLine()
                }
                val parser = Parser.default()
                val thisDetails = parser.parse(string) as JsonObject
                details = thisDetails[jsonObjectValute] as JsonObject
                CoroutineScope(Dispatchers.IO).launch{ saveValute(details as JsonObject)}
                return toArray()
            }
            return toArray()
        }
        else return toArray()
    }

    private fun toArray(): MutableList<CurrencyDetails>{
        val listValutes: MutableList<CurrencyDetails> = JsonArray()
        for(i in details!!.values)
            listValutes.add(Klaxon().parse<CurrencyDetails>(JsonObject(i as JsonObject).toJsonString())!!)
        return listValutes
    }

    private fun saveValute(details: JsonObject) {
        sPreferences.edit().putString(mDetailsActivity.APP_PREFERENCES, details.toJsonString()).apply()
        Log.i(TAG, "Exchange rates was successfully saved")
    }

    private fun isValuteSerialize(): Boolean {
        return sPreferences.contains(mDetailsActivity.APP_PREFERENCES)
    }

    public fun deleteValute() {
        val parser = Parser.default()
        details = parser.parse(StringBuilder("{}")) as JsonObject
        sPreferences.edit().remove(mDetailsActivity.APP_PREFERENCES).apply()
        Log.i(TAG, "Exchange rates was successfully deleted")
    }

    private fun getSavedDetails() {
        val q: String? = sPreferences.getString(mDetailsActivity.APP_PREFERENCES, "")
        val string = StringBuilder(q!!)
        if (string.isNotEmpty()) {
            val parser = Parser.default()
            try {
                 details = parser.parse(string) as JsonObject
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        /** TO DO: else throw exception*/
    }
}