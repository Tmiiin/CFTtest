package com.example.cfttest

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cfttest.convert.ConvertFragment
import com.example.cfttest.view.CurrencyDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BankActivity: AppCompatActivity(), ProgressBarRV, BankAdapter.OnValuteListener {

    val TAG = "BankFragment"
    lateinit var rootView: View
    lateinit var listView: RecyclerView //rv with details
    private var listOfDetails: MutableList<CurrencyDetails> =
        arrayListOf() //list of bank_material_card-views
    lateinit var sPreferences: SharedPreferences
    private lateinit var mPresenter: BankPresenter
    lateinit var progressBar: ProgressBar
    lateinit var detailsAdapter: BankAdapter
    lateinit var detailsIsEmpty: TextView
    lateinit var convertButton: TextView
    lateinit var syncButton: ImageView
    lateinit var container2: FrameLayout
    var APP_PREFERENCES = "bankData"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        sPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        mPresenter = BankPresenter(this, sPreferences, this)
        listView = findViewById<RecyclerView>(R.id.details_rv)
        syncButton = findViewById(R.id.sync)
        convertButton = findViewById(R.id.convert)
        progressBar = findViewById<ProgressBar>(R.id.details_progress_bar)
        detailsIsEmpty = findViewById<TextView>(R.id.details_is_empty)
        detailsIsEmpty.visibility = View.INVISIBLE
        listView.layoutManager = LinearLayoutManager(this)
        container2 = findViewById(R.id.details_container)
        detailsAdapter = BankAdapter(listOfDetails, this, this)
        listView.adapter = detailsAdapter
        showProgressBar(listView, detailsIsEmpty, progressBar)
        syncButton.setOnClickListener { CoroutineScope(IO).launch{onSyncButtonClick()} }
        CoroutineScope(IO).launch {
            setDetailsList()
            withContext(Main) {
                hideProgressBar(listView, detailsIsEmpty, progressBar, listOfDetails)
            }
        }
    }

    private suspend fun setDetailsList() {
        val list: MutableList<CurrencyDetails> = mPresenter.downloadBankDetails()
        listOfDetails.clear()
        for(i in list) {
            listOfDetails.add(i as CurrencyDetails)
        }
        withContext(Main) {
            detailsAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun onSyncButtonClick(){
            withContext(Main){showProgressBar(listView, detailsIsEmpty, progressBar)}
            mPresenter.deleteValute()
            mPresenter.downloadBankDetails()
            withContext(Main){hideProgressBar(listView, detailsIsEmpty, progressBar, listOfDetails)}
    }

    override fun onValuteClick(v: View?, position: Int) {
        hideAll(listView, detailsIsEmpty, progressBar)
        supportFragmentManager.beginTransaction().add(container2, "detail").replace(R.id.details_container, ConvertFragment(v)).addToBackStack(null).commit()
    }

}