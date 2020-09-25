package com.example.cfttest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.example.cfttest.convert.ConvertFragment
import com.example.cfttest.view.CurrencyDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection

class ListFragment(val sPreferences: SharedPreferences, val mContext: Context): Fragment(), ProgressBarRV, BankAdapter.OnValuteListener{

    var details: JsonObject? = null
    lateinit var listView: RecyclerView //rv with details
    private var listOfDetails: MutableList<CurrencyDetails> =
        arrayListOf() //list of bank_material_card-views
    private lateinit var mPresenter: BankPresenter
    lateinit var progressBar: ProgressBar
    lateinit var detailsAdapter: BankAdapter
    lateinit var detailsIsEmpty: TextView
    lateinit var syncButton: ImageView
    lateinit var container2: FrameLayout
    lateinit var rootView: View
    var APP_PREFERENCES = "bankData"
    val TAG = "ListFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mPresenter = BankPresenter(this, sPreferences, mContext)
        rootView = inflater.inflate(R.layout.details_layout, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView = view.findViewById<RecyclerView>(R.id.details_rv)
        syncButton = view.findViewById(R.id.sync)
        progressBar = view.findViewById<ProgressBar>(R.id.details_progress_bar)
        detailsIsEmpty = view.findViewById<TextView>(R.id.details_is_empty)
        detailsIsEmpty.visibility = View.INVISIBLE
        listView.layoutManager = LinearLayoutManager(mContext)
        container2 = view.findViewById(R.id.details_container)
        detailsAdapter = BankAdapter(listOfDetails, this, mContext)
        listView.adapter = detailsAdapter
        showProgressBar(listView, detailsIsEmpty, progressBar)
        syncButton.setOnClickListener { CoroutineScope(Dispatchers.IO).launch{onSyncButtonClick()} }
        CoroutineScope(Dispatchers.IO).launch {
            setDetailsList()
            withContext(Dispatchers.Main) {
                hideProgressBar(listView, detailsIsEmpty, progressBar, listOfDetails)
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
    private suspend fun setDetailsList() {
        val list: MutableList<CurrencyDetails> = mPresenter.downloadBankDetails()
        listOfDetails.clear()
        for(i in list) {
            listOfDetails.add(i as CurrencyDetails)
        }
        withContext(Dispatchers.Main) {
            detailsAdapter.notifyDataSetChanged()
        }
    }

    private suspend fun onSyncButtonClick(){
        withContext(Dispatchers.Main){showProgressBar(listView, detailsIsEmpty, progressBar)}
        mPresenter.deleteValute()
        mPresenter.downloadBankDetails()
        withContext(Dispatchers.Main){hideProgressBar(listView, detailsIsEmpty, progressBar, listOfDetails)}
    }

    override fun onValuteClick(v: View?, position: Int) {
        hideAll(listView, detailsIsEmpty, progressBar)
        fragmentManager!!.beginTransaction().add(ListFragment(sPreferences, mContext), TAG)
            .replace(R.id.details_container, ConvertFragment(v)).addToBackStack(null).commit()
    }

}