package com.ryanyu.basecore.activity

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.ryanyu.basecore.helper.RYLibSetting
import com.ryanyu.basecore.R
import com.ryanyu.basecore.fragment.RYBaseFragment

import io.reactivex.Observable
import io.reactivex.Observer

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

abstract class RYBaseActivity : FragmentActivity() {
    abstract fun getIvHeaderBackBtn(): ImageView?
    abstract fun getIvHeaderRightBtn(): ImageView?
    abstract fun getIvHeaderLeftBtn(): ImageView?
    abstract fun geTvHeaderContent(): View?
    abstract fun getTvHeaderTitle(): TextView?
    abstract fun getRootFragmentId(): Int

    private var pdRootLoadingDialog: ProgressDialog? = null
    private var stopAutoBack = false

    var doubleBackToExitPressedOnce = false

    abstract fun isAutoHiddenBack(): Boolean

    var fragmentActivityResultObserver: Observer<ArrayList<Any>>? = null

    fun initRoot() {
        initBackButton()
        initAutoBack()
    }

    private fun initAutoBack() {
        supportFragmentManager.addOnBackStackChangedListener {
            if (isAutoHiddenBack() && !stopAutoBack && supportFragmentManager.backStackEntryCount > 0) getIvHeaderBackBtn()?.visibility =
                    View.VISIBLE else getIvHeaderBackBtn()?.visibility = View.GONE
            if (!isLockDrawer && supportFragmentManager.backStackEntryCount > 0) ryHeaderMenuDrawerMenu?.getDlContenDrawer()?.setDrawerLockMode(
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            ) else ryHeaderMenuDrawerMenu?.getDlContenDrawer()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            if (ryBaseTabBar?.isAutoDisplayTabBar()!! && supportFragmentManager.backStackEntryCount > 0) isShowTabBar(
                false
            ) else isShowTabBar(true)
        }
    }

    fun isCloseAutoBack(stopAutoBack: Boolean) {
        this.stopAutoBack = stopAutoBack
    }

    fun setHeaderTitle(title: String?) {
        getTvHeaderTitle()?.text = title
    }

    fun findFragment(): RYBaseFragment? {
        return supportFragmentManager.findFragmentById(getRootFragmentId()) as? RYBaseFragment
    }

    fun getFragment(): RYBaseFragment? {
        return if (getRootFragmentId() == 99) null else supportFragmentManager.findFragmentById(getRootFragmentId()) as RYBaseFragment
    }

    fun isShowHeaderView(show: Boolean) {
        if (show) geTvHeaderContent()?.visibility = View.VISIBLE else geTvHeaderContent()?.visibility = View.GONE
    }

    fun isShowRightBtn(show: Boolean) {
        if (show) getIvHeaderRightBtn()?.visibility = View.VISIBLE else getIvHeaderRightBtn()?.visibility = View.GONE
    }

    fun isShowLeftBtn(show: Boolean) {
        if (show) getIvHeaderLeftBtn()?.visibility = View.VISIBLE else getIvHeaderLeftBtn()?.visibility = View.GONE
    }

    fun doubleBack() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        displayPopupMessage(resources.getString(R.string.global_back_again))
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    fun displayPopupMessage(message: String) {
        Toast.makeText(this@RYBaseActivity, message, Toast.LENGTH_SHORT).show()
    }

    fun initBackButton() {
        getIvHeaderBackBtn()?.setOnClickListener { if (!stopAutoBack) onBackPressed() }
    }

    fun switchRootFragment(type: Int?) {
        switchFragment(RYLibSetting.initRYFragmentModule?.initFragment(type!!)!!)
    }

    fun switchFragment(f: RYBaseFragment) {
        closeDrawerLayout()
        if (f.getFragmentType() === findFragment()?.getFragmentType()) return

        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val fragmentTransaction =
            supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.replace(getRootFragmentId(), f).commitAllowingStateLoss()

    }

    fun switchToDetailPage(f: RYBaseFragment) {
        supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .replace(getRootFragmentId(), f)
            .commit()
    }

    fun showProgressDialog() {
        dismissProgressDialog()
        pdRootLoadingDialog = ProgressDialog(this)
        pdRootLoadingDialog?.run {
            setMessage(resources.getString(R.string.global_loading))
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setCancelable(false)
            pdRootLoadingDialog?.show()
        }
    }

    fun dismissProgressDialog() {
        pdRootLoadingDialog?.let { if (it.isShowing) it.dismiss() }
        pdRootLoadingDialog = null
    }


    /* ----------------------- START ----------------------- */
    /**
     * Header DrawerLayout Control
     *
     * 1. implements RYHeaderMenuBtn
     * 2. call initMenuButton(this)
     **/
    var ryHeaderMenuDrawerMenu: RYHeaderMenuDrawerMenu? = null
    private var isLockDrawer = false


    interface RYHeaderMenuDrawerMenu {
        fun getDlContenDrawer(): DrawerLayout
        fun isLinkToMenuBtn(): Boolean
    }

    fun closeDrawerLayout() {
        ryHeaderMenuDrawerMenu?.getDlContenDrawer()?.run {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            }
        }
    }

    fun islockDrawerLayout(isLockDrawer: Boolean) {
        this.isLockDrawer = true
        if (this.isLockDrawer) ryHeaderMenuDrawerMenu?.getDlContenDrawer()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) else ryHeaderMenuDrawerMenu?.getDlContenDrawer()?.setDrawerLockMode(
            DrawerLayout.LOCK_MODE_UNLOCKED
        )
    }

    fun toggleDrawerLayout() {
        ryHeaderMenuDrawerMenu?.getDlContenDrawer()?.run {
            if (isDrawerOpen(GravityCompat.START)) closeDrawer(GravityCompat.START) else openDrawer(GravityCompat.START)
        }
    }


/* ----------------------- END ----------------------- */


/* ----------------------- START ----------------------- */
    /**
     * Header Menu Btn Control
     *
     * 1. implements RYHeaderMenuBtn
     * 2. call initMenuButton(this)
     **/
    var ryHeaderMenuBtn: RYHeaderMenuBtn? = null
    var onMenuBtnClickListener: OnMenuBtnClickListener? = null

    interface RYHeaderMenuBtn {
        fun getIvHeaderMenuBtn(): View?
    }

    interface OnMenuBtnClickListener {
        fun menuBtnClickListener()
    }

    fun initOnMenuBtnClickListener(onMenuBtnClickListener: OnMenuBtnClickListener?) {
        this.onMenuBtnClickListener = onMenuBtnClickListener
    }

    fun initMenuButton(ryHeaderMenuBtn: RYHeaderMenuBtn?) {
        this.ryHeaderMenuBtn = ryHeaderMenuBtn
        this.ryHeaderMenuBtn?.getIvHeaderMenuBtn()?.setOnClickListener {
            if (ryHeaderMenuDrawerMenu?.isLinkToMenuBtn()!!) toggleDrawerLayout()
            onMenuBtnClickListener?.menuBtnClickListener()
        }
    }

    fun isShowMenuBtn(show: Boolean) {
        if (show) ryHeaderMenuBtn?.getIvHeaderMenuBtn()?.visibility =
                View.VISIBLE else ryHeaderMenuBtn?.getIvHeaderMenuBtn()?.visibility = View.GONE
    }


    /**
     * ------------------------------------------------------------------------------------------------------------------------
     * Fragment with Activity Result (Activity part)
     *
     * 1. implements RYFragmentActivityResult
     * 2. call setRYFragmentActivityResult(this)
     *
     *
     * @param requestCode Int
     * @param resultCode Int
     * @param data Intent?
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragmentActivityResultObserver?.let {
            var activityResultObservable = Observable.create<ArrayList<Any>> {
                var temp: ArrayList<Any> = ArrayList()
                temp.add(requestCode)
                temp.add(resultCode)
                if (data != null) {
                    temp.add(data)
                }
                it.onNext(temp)
            }
            activityResultObservable?.subscribe(it)
        }
    }

    /**
     *
     *
     * @param fragmentActivityResultObserver Observer<ArrayList<Any>>
     */
    fun setActivityResultObserver(fragmentActivityResultObserver: Observer<ArrayList<Any>>) {
        this.fragmentActivityResultObserver = fragmentActivityResultObserver
    }


    /**
     * ------------------------------------------------------------------------------------------------------------------------------
     */



/* ----------------------- START ----------------------- */
    /**
     * Activity with Tabbar Control
     **/

    var ryBaseTabBar: RYBaseTabBar? = null
    var ryTabBarBtnNumber: RYTabBarBtnNumber? = null
    var ryTabBarStyle: RYTabBarStyle? = null

    interface RYBaseTabBar {
        fun getTabBar(): View?
        fun getTabBarBtn(): ArrayList<TextView>?
        fun getTabBarBtnTextTv(): ArrayList<TextView>?
        fun getTabBarBtnListener(): OnTabBarItemClickListener?
        fun getTabBarBtnSelectColor(): String?
        fun getTabBarBtnUnSelectColor(): String?
        fun isAutoDisplayTabBar(): Boolean?
    }

    interface RYTabBarBtnNumber {
        fun getTabBarBtnNumberTv(): ArrayList<TextView>?
    }

    interface RYTabBarStyle {
        fun getTabBarBtnText(): ArrayList<String>?
        fun getTabBarBtnImageId(): ArrayList<Int>?
        fun getTabBarBtnSelectedImageId(): ArrayList<Int>?
    }

    interface OnTabBarItemClickListener {
        fun itemBtnClick(position: Int)
    }

    fun updateTabBar() {
        for (i in 0 until ryBaseTabBar?.getTabBarBtnTextTv()?.size!!) {
            ryBaseTabBar?.getTabBarBtnTextTv()?.get(i)?.text = ryTabBarStyle?.getTabBarBtnText()?.get(i)
            ryBaseTabBar?.getTabBarBtnTextTv()?.get(i)
                ?.setCompoundDrawablesWithIntrinsicBounds(0, ryTabBarStyle?.getTabBarBtnImageId()?.get(i) ?: 0, 0, 0)
            ryBaseTabBar?.getTabBarBtnTextTv()?.get(i)
                ?.setTextColor(Color.parseColor(ryBaseTabBar?.getTabBarBtnUnSelectColor()))
        }
    }


    fun isShowTabBar(show: Boolean) {
        if (show) ryBaseTabBar?.getTabBar()?.visibility = View.VISIBLE else ryBaseTabBar?.getTabBar()?.visibility =
                View.GONE
    }

    fun setRYBaseTabBar(ryBaseTabBar: RYBaseTabBar) {
        this.ryBaseTabBar = ryBaseTabBar
        for (i in 0 until ryBaseTabBar?.getTabBarBtn()?.size!!) {
            this.ryBaseTabBar?.getTabBarBtn()?.get(i)?.setOnClickListener { view -> tabBarOnClickListener(i) }
        }
    }

    fun setRYTabBarBtnNumber(ryTabBarBtnNumber: RYTabBarBtnNumber) {
        this.ryTabBarBtnNumber = ryTabBarBtnNumber;
    }

    fun setRYTabBarStyle(ryTabBarStyle: RYTabBarStyle) {
        this.ryTabBarStyle = ryTabBarStyle;
        updateTabBar()
    }

    fun tabBarOnClickListener(position: Int) {
        ryBaseTabBar?.getTabBarBtnListener()?.itemBtnClick(position)
    }

    fun setFooterTabNumberVisibility(tabNumber: Int, onOff: Int) {
        ryTabBarBtnNumber?.getTabBarBtnNumberTv()?.get(tabNumber)?.visibility = onOff
    }

    fun setFooterTabNumber(tabNumber: Int, num: String) {
        ryTabBarBtnNumber?.getTabBarBtnNumberTv()?.get(tabNumber)?.text = num
    }

    fun unSelectAllTabBar() {
        for (i in 0 until ryBaseTabBar?.getTabBarBtnTextTv()?.size!!) {
            ryBaseTabBar?.getTabBarBtnTextTv()?.get(i)
                ?.setCompoundDrawablesWithIntrinsicBounds(0, ryTabBarStyle?.getTabBarBtnImageId()?.get(i) ?: 0, 0, 0)
            ryBaseTabBar?.getTabBarBtnTextTv()?.get(i)
                ?.setTextColor(Color.parseColor(ryBaseTabBar?.getTabBarBtnUnSelectColor()))
        }

    }

    fun selectTabBar(tabNumber: Int) {
        unSelectAllTabBar()
        ryBaseTabBar?.getTabBarBtnTextTv()?.get(tabNumber)?.setCompoundDrawablesWithIntrinsicBounds(
            0,
            ryTabBarStyle?.getTabBarBtnSelectedImageId()?.get(tabNumber) ?: 0,
            0,
            0
        )
        ryBaseTabBar?.getTabBarBtnTextTv()?.get(tabNumber)
            ?.setTextColor(Color.parseColor(ryBaseTabBar?.getTabBarBtnSelectColor()))
    }
/* ----------------------- END ----------------------- */

}
