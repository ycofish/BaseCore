package com.ryanyu.basecore.observer

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import com.ryanyu.basecore.R
import com.ryanyu.basecore.listener.RYObserverEasyListener
import com.ryanyu.basecore.listener.RYObserverFullStatusListener
import com.ryanyu.basecore.listener.RyObserverSingleStatusListener
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * Update 2019-01-09
 *
 * ██████╗ ██╗   ██╗ █████╗ ███╗   ██╗    ██╗   ██╗██╗   ██╗    ██╗     ██╗██████╗ ██████╗  █████╗ ██████╗ ██╗   ██╗
 * ██╔══██╗╚██╗ ██╔╝██╔══██╗████╗  ██║    ╚██╗ ██╔╝██║   ██║    ██║     ██║██╔══██╗██╔══██╗██╔══██╗██╔══██╗╚██╗ ██╔╝
 * ██████╔╝ ╚████╔╝ ███████║██╔██╗ ██║     ╚████╔╝ ██║   ██║    ██║     ██║██████╔╝██████╔╝███████║██████╔╝ ╚████╔╝
 * ██╔══██╗  ╚██╔╝  ██╔══██║██║╚██╗██║      ╚██╔╝  ██║   ██║    ██║     ██║██╔══██╗██╔══██╗██╔══██║██╔══██╗  ╚██╔╝
 * ██║  ██║   ██║   ██║  ██║██║ ╚████║       ██║   ╚██████╔╝    ███████╗██║██████╔╝██║  ██║██║  ██║██║  ██║   ██║
 * ╚═╝  ╚═╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═══╝       ╚═╝    ╚═════╝     ╚══════╝╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
 *
 *
 * 88888888ba          db          ad88888ba   88888888888    ,ad8888ba,     ,ad8888ba,    88888888ba   88888888888
 * 88      "8b        d88b        d8"     "8b  88            d8"'    `"8b   d8"'    `"8b   88      "8b  88
 * 88      ,8P       d8'`8b       Y8,          88           d8'            d8'        `8b  88      ,8P  88
 * 88aaaaaa8P'      d8'  `8b      `Y8aaaaa,    88aaaaa      88             88          88  88aaaaaa8P'  88aaaaa
 * 88""""""8b,     d8YaaaaY8b       `"""""8b,  88"""""      88             88          88  88""""88'    88"""""
 * 88      `8b    d8""""""""8b            `8b  88           Y8,            Y8,        ,8P  88    `8b    88
 * 88      a8P   d8'        `8b   Y8a     a8P  88            Y8a.    .a8P   Y8a.    .a8P   88     `8b   88
 * 88888888P"   d8'          `8b   "Y88888P"   88888888888    `"Y8888Y"'     `"Y8888Y"'    88      `8b  88888888888
 *
 *
 * Created by Ryan Yu.
 */


class RYEasyObserver<T> : Observer<T> {
    private var progressDialog: ProgressDialog? = null
    private var listener: RYObserverEasyListener<T>? = null
    private var fullListener: RYObserverFullStatusListener<T>? = null
    private var singleListener: RyObserverSingleStatusListener<T>? = null
    private var context: Context? = null

    constructor(context: Context?, listener: RYObserverEasyListener<T>?) {
        this.listener = listener
        this.context = context
    }

    constructor(context: Context?, fullListener: RYObserverFullStatusListener<T>?) {
        this.fullListener = fullListener
        this.context = context
    }

    constructor(context: Context?, singleListener: RyObserverSingleStatusListener<T>?) {
        this.singleListener = singleListener
        this.context = context
    }

    override fun onSubscribe(d: Disposable) {
        Log.d(TAG, "onSubscribe: ")
        showProgressDialog()
    }

    override fun onNext(t: T) {
        fullListener?.onNext(t!!)
        singleListener?.onNext(t!!)
        listener?.onNext(t!!)
    }

    override fun onError(e: Throwable) {
        dismissProgressDialog()
        Log.e(TAG, "onError: ", e)

        fullListener?.onError()
        singleListener?.onError()

    }

    override fun onComplete() {
        dismissProgressDialog()
        Log.d(TAG, "onComplete: ")
        fullListener?.onFinish()

    }

    fun showProgressDialog() {
        if (progressDialog != null)
            if (progressDialog!!.isShowing)
                progressDialog!!.dismiss()

        progressDialog = ProgressDialog(context)
        progressDialog!!.setMessage(context!!.resources.getString(R.string.global_loading))
        progressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        progressDialog!!.setCancelable(false)

        progressDialog!!.show()

    }

    fun dismissProgressDialog() {
        if (progressDialog != null)
            if (progressDialog!!.isShowing) {
                progressDialog!!.dismiss()
            }
        progressDialog = null
    }

    companion object {
        private val TAG = "EasyObserver"
    }

}