package com.epiccomm.fsee.ryanlib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import com.ryanyu.basecore.helper.RYLibSetting
import com.ryanyu.basecore.listener.RYObserverEasyListener
import com.ryanyu.basecore.observer.RYEasyObserver
import com.ryanyu.basecore.activity.RYBaseActivity
import com.ryanyu.basecore.listener.RYWaitListener
import com.ryanyu.basecore.utils.RYBox


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

abstract class RYBaseFragment : Fragment() {
    var ctx: Context? = activity


    abstract fun getFragmentType(): Int

    fun getFragment(): RYBaseFragment? {
        return getContent()?.getNowDisplayFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ctx = activity
    }

    fun switchRootFragment(f: Int) {
        getContent()?.switchRootFragment(f)
    }


    fun switchToDetailPage(f: RYBaseFragment) {
        getContent()?.switchToDetailPage(f)
    }

    fun switchToAddDetailPage(f: RYBaseFragment) {
        getContent()?.switchToAddDetailPage(f)
    }

    fun getContent(): RYBaseActivity? {
        return if (ctx is RYBaseActivity) (ctx as RYBaseActivity) else null
    }

/* ----------------------- START ----------------------- */
    /**
     * Fragment with menuBtn Control
     **/
    var ryHeaderMenuBtn: RYHeaderMenuBtn? = null

    interface RYHeaderMenuBtn {
        fun isShowMenuBtn(): Boolean
    }

    fun setRYHeaderMenuBtn(ryHeaderMenuBtn: RYHeaderMenuBtn?) {
        this.ryHeaderMenuBtn = ryHeaderMenuBtn
        setRYMenuBtn()
    }

    fun setRYMenuBtn() {
        if (ryHeaderMenuBtn?.isShowMenuBtn()!!) getContent()?.isShowMenuBtn(true) else getContent()?.isShowMenuBtn(false)
    }

/* ----------------------- END ----------------------- */


/* ----------------------- START ----------------------- */
    /**
     * Fragment with TabBar Control
     **/
    var ryBaseTabBar: RYBaseTabBar? = null

    interface RYBaseTabBar {
        fun isShowTarBarView(): Boolean?
    }

    fun setRYBaseTabBar(ryBaseTabBar: RYBaseTabBar?) {
        this.ryBaseTabBar = ryBaseTabBar
        setRYTarBar(ryBaseTabBar?.isShowTarBarView())
    }

    fun setRYTarBar(isShow: Boolean?) {
        if (isShow == true) getContent()?.isShowTabBar(true) else getContent()?.isShowTabBar(false)
    }

    fun setRYTabBarBtnNumberVisibility(tabNumber: Int, onOff: Int) {
        getContent()?.setRYTabBarBtnNumberVisibility(tabNumber,onOff)
    }

    fun setRYTabBarBtnNumber(tabNumber: Int, num: String) {
        getContent()?.setRYTabBarBtnNumber(tabNumber,num)
    }

    fun setRYTabBarBtnNumberWithZeroCheck(tabNumber: Int, num: String) {
        getContent()?.setRYTabBarBtnNumberWithZeroCheck(tabNumber,num)
    }


/* ----------------------- END ----------------------- */


/* ----------------------- START ryHeaderMenuBtn */
    /**
     * Fragment with Activity Result
     *
     * 1. implements RYFragmentActivityResult
     * 2. call setRYFragmentActivityResult(this)
     **/
    var ryFragmentActivityResult: RYFragmentActivityResult? = null

    fun setRYFragmentActivityResult(ryFragmentActivityResult: RYFragmentActivityResult) {
        this.ryFragmentActivityResult = ryFragmentActivityResult
        getActivityResult(getActivityResultObserver())
    }

    interface RYFragmentActivityResult {
        fun onRYFragmentActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    }

    private fun getActivityResult(observer: io.reactivex.Observer<ArrayList<Any>>) {
        getContent()?.setActivityResultObserver(observer)
    }

    fun getActivityResultObserver(): io.reactivex.Observer<ArrayList<Any>> {
        var listener = object : RYObserverEasyListener<ArrayList<Any>> {
            override fun onNext(t: ArrayList<Any>) {
                ryFragmentActivityResult?.onRYFragmentActivityResult(
                    t.get(0) as Int,
                    t.get(1) as Int,
                    t.get(2) as Intent
                )
            }
        }
        return ctx?.let { RYEasyObserver(it, listener) } as io.reactivex.Observer<ArrayList<Any>>
    }
/* ----------------------- END ----------------------- */

    fun setTitle(title: String?) {
        getContent()?.setHeaderTitle(title)
    }


    fun getLeftBtn(): View? {
        return getContent()?.ryBaseHeader?.getHeaderLeftBtn()
    }

    fun getBackButton(): View? {
        return getContent()?.ryBaseHeader?.getHeaderBackBtn()
    }

    fun getRightBtn(): View? {
        return getContent()?.ryBaseHeader?.getHeaderRightBtn()
    }

    fun initRyBaseHeader() {
        isShowLeftBtn(ryBaseHeader?.isShowLeftBtn()!!)
        isShowRightBtn(ryBaseHeader?.isShowRightBtn()!!)
        setHeaderView()
        setTitle(ryBaseHeader?.setHeaderTitle())
        setBackEvent()
    }

    fun setAutoHandleBackBtnDisplay(boolean: Boolean) {
        getContent()?.setAutoHandleBackBtnDisplay(boolean)
    }

    fun setHeaderView() {
        if (ryBaseHeader?.isShowHeaderView()!!) getContent()?.isShowHeaderView(true) else getContent()?.isShowHeaderView(
            false
        )
    }

    fun setBackEvent() {
        if (ryBaseHeader?.isSetBackBtn()!! && RYLibSetting.headerBackBtnIcon != null)
            (getBackButton() as ImageView)?.run {
                visibility = View.VISIBLE
                setImageDrawable(ContextCompat.getDrawable(activity!!, RYLibSetting.headerBackBtnIcon!!))
                setOnClickListener { activity!!.onBackPressed() }
            }
    }

    fun isShowRightBtn(boolean: Boolean) {
        getContent()?.isShowRightBtn(boolean)
    }

    fun isShowLeftBtn(boolean: Boolean) {
        getContent()?.isShowLeftBtn(boolean)
    }

    var ryBaseHeader: RYBaseHeader? = null

    interface RYBaseHeader {
        fun isShowHeaderView(): Boolean
        fun isShowLeftBtn(): Boolean
        fun isShowRightBtn(): Boolean
        fun isSetBackBtn(): Boolean
        fun setHeaderTitle(): String?
    }

    fun setRYBaseHeader(ryBaseHeader: RYBaseHeader) {
        this.ryBaseHeader = ryBaseHeader
    }


    /* ----------------------- START ryBox */

    /**
     * ryBox Function
     *
     */

    fun ryToast(text: String?) {
        if (text == null) return
        RYBox?.easyToast(ctx, text)
    }

    fun ryToast(id: Int?) {
        if (id == null) return
        RYBox?.easyToast(ctx, RYBox?.easyText(ctx, id))
    }

    fun ryString(id: Int): String = RYBox?.easyText(ctx, id)


    fun ryLoading() {
        RYBox?.showProgressDialog(ctx)
    }

    fun ryLoadingDismiss() {
        RYBox?.dismissProgressDialog()
    }

    fun <T> ryWait(sec: Long, function: () -> T) {
        RYBox.ryWait(sec, function)
    }

    fun <T> ryWait(sec: Long, value: T, function: (value: T) -> Unit) {
        RYBox.ryWait(sec, value, function)
    }

    fun ryWait(sec: Long, listener: RYWaitListener) {
        RYBox.ryWait(sec, listener)
    }

    /* ----------------------- END ----------------------- */


}