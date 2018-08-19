package com.example.kirill.currencylist.view.currencylist

import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView

class ListItemAnimator(private val onMovedFinishedListener: () -> Unit) : DefaultItemAnimator() {

    override fun onMoveFinished(item: RecyclerView.ViewHolder?) {
//        onMovedFinishedListener()
    }
}