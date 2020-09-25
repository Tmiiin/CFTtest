package com.example.cfttest

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Klaxon
import com.beust.klaxon.Parser
import com.example.cfttest.view.CurrencyDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.LocalDate

class BankPresenter( //for operations with details
    private var mDetailsActivity: ListFragment,
    var sPreferences: SharedPreferences,
    private var mContext: Context
) : Fragment() {

    val jsonObjectValute: String = "Valute"
    val TAG = "RatesPresenter"
    var APP_PREFERENCES_DATE: String = "lastReload"


    @SuppressLint("NewApi")
    suspend fun downloadBankDetails(): MutableList<CurrencyDetails> {
        if (mDetailsActivity.details.isNullOrEmpty()) {
            if (isValuteSerialize() && LocalDate.now().minusDays(1).isAfter(getSavedDate()))
                getSavedDetails()
            else {
                Log.i(TAG, "Started download new exchange rates")
                var connection = Request()
                val json = BufferedReader(InputStreamReader(connection.getConnection().inputStream))
                val string = StringBuilder()
                var stringNow = json.readLine()
                while (stringNow != null) {
                    string.append(stringNow)
                    stringNow = json.readLine()
                }
                val parser = Parser.default()
                connection.closeConnection()
                val thisDetails = parser.parse(string) as JsonObject
                if(thisDetails.containsKey(jsonObjectValute)) {
                    mDetailsActivity.details = thisDetails[jsonObjectValute] as JsonObject
                    CoroutineScope(Dispatchers.IO).launch { saveValute(mDetailsActivity.details as JsonObject) }
                    saveNowDate()
                }
                return toArray()
            }
            return toArray()
        } else return toArray()
    }

    private fun toArray(): MutableList<CurrencyDetails> {
        val listValutes: MutableList<CurrencyDetails> = JsonArray()
        return if(!mDetailsActivity.details.isNullOrEmpty()) {
            for (i in mDetailsActivity.details!!.values)
                listValutes.add(Klaxon().parse<CurrencyDetails>(JsonObject(i as JsonObject).toJsonString())!!)
            listValutes
        } else JsonArray()
    }

    private fun saveValute(details: JsonObject) {
        sPreferences.edit().putString(mDetailsActivity.APP_PREFERENCES, details.toJsonString())
            .apply()
        Log.i(TAG, "Exchange rates was successfully saved")
    }

    private fun isValuteSerialize(): Boolean {
        if(sPreferences.contains(mDetailsActivity.APP_PREFERENCES))
        return sPreferences.getString(mDetailsActivity.APP_PREFERENCES, "")!!.length > 1
        else return false
    }

    public fun deleteValute() {
        val parser = Parser.default()
        mDetailsActivity.details = parser.parse(StringBuilder("{}")) as JsonObject
        sPreferences.edit().putString(mDetailsActivity.APP_PREFERENCES, "").apply()
        Log.i(TAG, "Exchange rates was successfully deleted")
    }

    private fun getSavedDetails() {
        val q: String? = sPreferences.getString(mDetailsActivity.APP_PREFERENCES, "")
        val string = StringBuilder(q!!)
        if (string.isNotEmpty()) {
            val parser = Parser.default()
            try {
                mDetailsActivity.details = parser.parse(string) as JsonObject
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }
        /** TO DO: else throw exception*/
    }

    fun saveNowDate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            sPreferences.edit().putString(APP_PREFERENCES_DATE, LocalDate.now().toString()).apply()
        } else throw RuntimeException("can't save date")
    }

    fun getSavedDate(): LocalDate {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            return LocalDate.parse(sPreferences.getString(APP_PREFERENCES_DATE, ""))
        } else throw RuntimeException("can't get date")
    }
}
