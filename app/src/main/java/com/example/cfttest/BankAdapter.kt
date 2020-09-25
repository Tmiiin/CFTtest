package com.example.cfttest

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cfttest.view.CurrencyDetails

class BankAdapter(
    private var items: List<CurrencyDetails>,
    private val onValuteListener: OnValuteListener,
    val context: Context
) :
    RecyclerView.Adapter<BankAdapter.BankViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.bank_material_card,
            parent,
            false
        ) as View
        return BankViewHolder(v, onValuteListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class BankViewHolder(view: View, private val mOnValuteListener: OnValuteListener) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        init{
            view.setOnClickListener(this)
        }
        var charCode: TextView = view.findViewById(R.id.valute_char_code)
        var valuteName: TextView = view.findViewById(R.id.valute_name)
        var nominal: TextView = view.findViewById(R.id.valute_nominal)
        var valutePrev: TextView = view.findViewById(R.id.valute_prev)
        var valuteNow: TextView = view.findViewById(R.id.valute_now)
        var arrow: ImageView = view.findViewById(R.id.arrow)

        fun bind(valute: CurrencyDetails) {
            charCode.text = valute.CharCode
            valuteName.text = valute.Name
            nominal.text = valute.Nominal.toString()
            valutePrev.text = valute.Previous.toString()
            valuteNow.text = valute.Value.toString()
            if (valute.Previous < valute.Value)
                arrow.setBackgroundColor(Color.parseColor("#ADFF2F"))
            else arrow.setBackgroundColor(Color.parseColor("#FA8072"))

        }

        override fun onClick(p0: View?) {
            mOnValuteListener.onValuteClick(p0, adapterPosition)
        }

    }
    interface OnValuteListener{
        fun onValuteClick(v: View?, position: Int)
    }
}

