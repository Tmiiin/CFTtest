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
import android.view.View as View

class ConvertFragment(val item: View?) : Fragment() {

    lateinit var convertButton: Button
    lateinit var exchangeRate: TextView
    lateinit var editText: EditText
    lateinit var convertResult: TextView
    lateinit var buttonGoOut: Button
    val TAG = "ConvertFragment"
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
        convertButton = view!!.findViewById(R.id.convert_button)
        exchangeRate = view.findViewById(R.id.convert_value)
        editText = view.findViewById(R.id.convert_edit_text)
        convertResult = view.findViewById(R.id.convert_result)
        buttonGoOut = view.findViewById(R.id.convert_go_out)
        convertButton.setOnClickListener() { onConvert() }
        buttonGoOut.setOnClickListener() { goOut() }
        exchangeRate.text = item!!.findViewById<TextView>(R.id.valute_now).text
        super.onViewCreated(view, savedInstanceState)
    }
    private fun onConvert() {
        if(!editText.text.isNullOrEmpty())
        {
            var nominalString: String = item!!.findViewById<TextView>(R.id.valute_nominal).text as String
            val nominal = nominalString.toDouble()
            val value = exchangeRate.text.toString().toDouble()
            var userSum: Double = editText.text.toString().toDouble()
            val koef: Double = (value / nominal)
           // val regex = ",{2}".toRegex()
            val match = (koef * userSum)
            convertResult.text = match.toString()//.value
        }
    }
}




