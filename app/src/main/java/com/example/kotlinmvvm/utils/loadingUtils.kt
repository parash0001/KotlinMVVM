package com.example.kotlinmvvm.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.kotlinmvvm.R

class LoadingUtils (val activity: Activity) {
    lateinit var alertDialog: AlertDialog

    fun showLoading(){
        var dialogView= activity.layoutInflater.inflate(R.layout.loading_dialog,null)
        var builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog.show()
    }

    fun dismiss(){
        alertDialog.dismiss()
    }
}