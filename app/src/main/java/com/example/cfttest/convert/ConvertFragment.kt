package com.example.cfttest.convert

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.cfttest.R
import java.lang.StringBuilder
import android.view.View as View

class ConvertFragment(val item: View?) : Fragment() {

    lateinit var convertButton: Button
    lateinit var exchangeRate: TextView
    lateinit var editText: EditText
    lateinit var convertResult: TextView
    lateinit var convertNominal: TextView
    lateinit var buttonGoOut: Button
    val TAG = "ConvertFragment"
    val sorry: String = "Извините, но это не конвертируется"
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.convert_layout, container, false)
        return rootView
    }

    private fun goOut() {
        Log.i(TAG,"Go out")
        fragmentManager?.popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val charCodes: TextView = view.findViewById(R.id.convert_char_code)
        val toCharCode: TextView = item!!.findViewById(R.id.valute_char_code)
        Log.i(TAG, toCharCode.text.toString())
        charCodes.text = StringBuilder("RUB -> ").append(toCharCode.text.toString()).toString()
        convertButton = view!!.findViewById(R.id.convert_button)
        exchangeRate = view.findViewById(R.id.convert_value)
        editText = view.findViewById(R.id.convert_edit_text)
        convertResult = view.findViewById(R.id.convert_result)
        convertNominal = view.findViewById<TextView>(R.id.convert_nominal)
        var valuteName = item.findViewById<TextView>(R.id.valute_name)
        var convertHint = view.findViewById<TextView>(R.id.convert_hint)
        var q: TextView = item.findViewById(R.id.valute_nominal)
        convertHint.text = StringBuilder("Из расчета за ").append(q.text.toString()).append(" ").append(valuteName.text.toString()).toString()
        buttonGoOut = view.findViewById(R.id.convert_go_out)
        convertButton.setOnClickListener() { onConvert() }
        buttonGoOut.setOnClickListener() { goOut() }
        exchangeRate.text = item!!.findViewById<TextView>(R.id.valute_now).text
        super.onViewCreated(view, savedInstanceState)
    }
    private fun onConvert() {
        if(!editText.text.isNullOrEmpty())
        {
            try {
                editText.text.toString().toDouble()
                var nominalString: String =
                    item!!.findViewById<TextView>(R.id.valute_nominal).text as String
                val nominal = nominalString.toDouble()
                val value = exchangeRate.text.toString().toDouble()
                var userSum: Double = editText.text.toString().toDouble()
                val koef: Double = (nominal / value)
                val match = String.format("%.3f", (koef * userSum))
                convertResult.text = match//.value
            }
            catch(e: NumberFormatException){
                Log.e(TAG, e.stackTrace.toString())
                convertResult.text = sorry
            }
        }
    }
}




