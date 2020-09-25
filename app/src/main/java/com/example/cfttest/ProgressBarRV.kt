package com.example.cfttest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface ProgressBarRV {

    fun showProgressBar(listView: RecyclerView, notesIsEmpty: TextView, progressBar: ProgressBar) {
        listView.visibility = View.INVISIBLE
        notesIsEmpty.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar(listView: RecyclerView, isEmpty: TextView, progressBar: ProgressBar, listOfNotes: List<Any>) {
        progressBar.visibility = View.INVISIBLE
        if(listOfNotes.isEmpty())
            isEmpty.visibility = View.VISIBLE
        else  listView.visibility = View.VISIBLE
    }

    fun hideAll(listView: RecyclerView, isEmpty: TextView, progressBar: ProgressBar){
        listView.visibility = View.INVISIBLE
        isEmpty.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
    }
}