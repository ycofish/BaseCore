package com.ryanyu.basecore.observer

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import com.ryanyu.basecore.R
import com.ryanyu.basecore.listener.RYObserverEasyCancelListener
import com.ryanyu.basecore.listener.RYObserverEasyListener
import com.ryanyu.basecore.listener.RYObserverFullStatusListener
import com.ryanyu.basecore.listener.RyObserverSingleStatusListener
import com.ryanyu.basecore.utils.RYBox.TAG
import com.ryanyu.basecore.utils.RYBox.dismissProgressDialog
import com.ryanyu.basecore.utils.RYBox.showProgressDialog
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
    private var cancelListener: RYObserverEasyCancelListener<T>? = null
    private var fullListener: RYObserverFullStatusListener<T>? = null
    private var singleListener: RyObserverSingleStatusListener<T>? = null
    private var context: Context? = null
    private var cancelLoading = false
    private var startLoading = true
    private var endLoading = true

    /**
     * only onNext and cancel
     *
     * @param context Context?
     * @param listener RYObserverEasyCancelListener<T>?
     * @constructor
     */
    constructor(context: Context?, listener: RYObserverEasyCancelListener<T>?) {
        this.cancelListener = cancelListener
        this.context = context
    }


    /**
     * only onNext
     *
     * @param context Context?
     * @param listener RYObserverEasyListener<T>?
     * @constructor
     */
    constructor(context: Context?, listener: RYObserverEasyListener<T>?) {
        this.listener = listener
        this.context = context
    }

    /**
     * onNext onFinish onError
     *
     * @param context Context?
     * @param fullListener RYObserverFullStatusListener<T>?
     * @constructor
     */
    constructor(context: Context?, fullListener: RYObserverFullStatusListener<T>?) {
        this.fullListener = fullListener
        this.context = context
    }

    /**
     * onNext and onError
     *
     * @param context Context?
     * @param singleListener RyObserverSingleStatusListener<T>?
     * @constructor
     */
    constructor(context: Context?, singleListener: RyObserverSingleStatusListener<T>?) {
        this.singleListener = singleListener
        this.context = context
    }


    /**
     * only onNext/Cancel and control isShowLoading
     *
     * @param context Context?
     * @param listener RYObserverEasyListener<T>?
     * @param cancelLoading Boolean
     * @constructor
     */
    constructor(context: Context?, listener: RYObserverEasyCancelListener<T>?, cancelLoading: Boolean) {
        this.cancelListener = listener
        this.context = context
        this.cancelLoading = cancelLoading
    }


    /**
     * only onNext and control isShowLoading
     *
     * @param context Context?
     * @param listener RYObserverEasyListener<T>?
     * @param cancelLoading Boolean
     * @constructor
     */
    constructor(context: Context?, listener: RYObserverEasyListener<T>?, cancelLoading: Boolean) {
        this.listener = listener
        this.context = context
        this.cancelLoading = cancelLoading
    }

    /**
     * onNext onFinish onError and control isShowLoading
     *
     * @param context Context?
     * @param fullListener RYObserverFullStatusListener<T>?
     * @param cancelLoading Boolean
     * @constructor
     */
    constructor(context: Context?, fullListener: RYObserverFullStatusListener<T>?, cancelLoading: Boolean) {
        this.fullListener = fullListener
        this.context = context
        this.cancelLoading = cancelLoading
    }

    /**
     * onNext and onError and control isShowLoading
     *
     * @param context Context?
     * @param singleListener RyObserverSingleStatusListener<T>?
     * @param cancelLoading Boolean
     * @constructor
     */
    constructor(context: Context?, singleListener: RyObserverSingleStatusListener<T>?, cancelLoading: Boolean) {
        this.singleListener = singleListener
        this.context = context
        this.cancelLoading = cancelLoading
    }


    /**
     * only onNext/Cancel and control start/end loading
     *
     * @param context Context?
     * @param listener RYObserverEasyListener<T>?
     * @param startLoading Boolean
     * @param endLoading Boolean
     * @constructor
     */
    constructor(context: Context?, listener: RYObserverEasyCancelListener<T>?, startLoading: Boolean, endLoading: Boolean) {
        this.cancelListener = listener
        this.context = context
        this.startLoading = startLoading
        this.endLoading = endLoading

        if(!startLoading && !endLoading){
            this.cancelLoading = true
        }
    }

    /**
     * only onNext and control start/end loading
     *
     * @param context Context?
     * @param listener RYObserverEasyListener<T>?
     * @param startLoading Boolean
     * @param endLoading Boolean
     * @constructor
     */
    constructor(context: Context?, listener: RYObserverEasyListener<T>?, startLoading: Boolean, endLoading: Boolean) {
        this.listener = listener
        this.context = context
        this.startLoading = startLoading
        this.endLoading = endLoading

        if(!startLoading && !endLoading){
            this.cancelLoading = true
        }
    }

    /**
     * onNext onFinish onError and control start/end loading
     *
     * @param context Context?
     * @param fullListener RYObserverFullStatusListener<T>?
     * @param startLoading Boolean
     * @param endLoading Boolean
     * @constructor
     */
    constructor(
        context: Context?,
        fullListener: RYObserverFullStatusListener<T>?,
        startLoading: Boolean,
        endLoading: Boolean
    ) {
        this.fullListener = fullListener
        this.context = context
        this.startLoading = startLoading
        this.endLoading = endLoading

        if(!startLoading && !endLoading){
            this.cancelLoading = true
        }
    }

    /**
     * onNext and onError and control start/end loading
     *
     * @param context Context?
     * @param singleListener RyObserverSingleStatusListener<T>?
     * @param startLoading Boolean
     * @param endLoading Boolean
     * @constructor
     */
    constructor(
        context: Context?,
        singleListener: RyObserverSingleStatusListener<T>?,
        startLoading: Boolean,
        endLoading: Boolean
    ) {
        this.singleListener = singleListener
        this.context = context
        this.startLoading = startLoading
        this.endLoading = endLoading

        if(!startLoading && !endLoading){
            this.cancelLoading = true
        }
    }

    override fun onSubscribe(d: Disposable) {
        cancelListener?.onSubscribe(d)
        Log.d(TAG, "onSubscribe: ")
        if (cancelLoading) {
            return
        }

        if (!startLoading) {
            return
        }

        showProgressDialog()

    }

    override fun onNext(t: T) {
        fullListener?.onNext(t!!)
        singleListener?.onNext(t!!)
        listener?.onNext(t!!)
        cancelListener?.onNext(t!!)
    }

    override fun onError(e: Throwable) {
        dismissProgressDialog()
        Log.e(TAG, "onError: ", e)
        fullListener?.onError()
        singleListener?.onError()

    }

    override fun onComplete() {
        Log.d(TAG, "onComplete: ")
        fullListener?.onFinish()
        if (cancelLoading) {
            return
        }

        if(!endLoading){
            return
        }

        dismissProgressDialog()

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
        private val TAG = "RYAN EasyObserver"
    }

}