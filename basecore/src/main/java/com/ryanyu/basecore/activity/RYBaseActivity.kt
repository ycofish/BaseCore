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

    /**
     * set back button for base core control
     *
     * @return ImageView?
     */
    abstract fun getIvHeaderBackBtn(): ImageView?

    /**
     * set header right button for base core control
     *
     * @return ImageView?
     */
    abstract fun getIvHeaderRightBtn(): ImageView?

    /**
     * set header left button for base core control
     *
     * @return ImageView?
     */
    abstract fun getIvHeaderLeftBtn(): ImageView?

    /**
     * set header view for base core control
     *
     * @return View?
     */
    abstract fun geTvHeaderContent(): View?

    /**
     * set header title textview for base core control
     *
     * @return TextView?
     */
    abstract fun getTvHeaderTitle(): TextView?

    /**
     * set fragment for base core control
     *
     * @return Int?
     */
    abstract fun getRootFragmentId(): Int

    /**
     * set BackStack by base core control
     *
     * @return Boolean?
     */
    abstract fun isAutoHiddenBack(): Boolean


    private var stopAutoBack = false

    var doubleBackToExitPressedOnce = false
    var fragmentActivityResultObserver: Observer<ArrayList<Any>>? = null

    /**
     * init base core
     *
     * !!!!!!must call!!!!
     */
    fun initRoot() {
        initBackButton()
        initAutoBack()
    }

    private fun initBackButton() {
        getIvHeaderBackBtn()?.setOnClickListener { if (!stopAutoBack) onBackPressed() }
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

    private fun switchFragment(f: RYBaseFragment) {
        closeDrawerLayout()
        if (f.getFragmentType() === getNowDisplayFragment()?.getFragmentType()) return

        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val fragmentTransaction =
            supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.replace(getRootFragmentId(), f).commitAllowingStateLoss()
    }

    /**
     * Can stop/start the base core control BackStack
     *
     * @param stopAutoBack Boolean
     */
    fun isCloseAutoBack(stopAutoBack: Boolean) {
        this.stopAutoBack = stopAutoBack
    }


    /**
     * set the header title
     *
     * @param title String?
     */
    fun setHeaderTitle(title: String?) {
        getTvHeaderTitle()?.text = title
    }

    /**
     * find fragment of now show
     *
     * @return RYBaseFragment?
     */
    fun getNowDisplayFragment(): RYBaseFragment? {
        return if (getRootFragmentId() == 99) null else supportFragmentManager.findFragmentById(getRootFragmentId()) as RYBaseFragment
    }

    /**
     * set the headerview visibility
     *
     * @param show Boolean?
     */
    fun isShowHeaderView(show: Boolean) {
        if (show) geTvHeaderContent()?.visibility = View.VISIBLE else geTvHeaderContent()?.visibility = View.GONE
    }

    /**
     * set the header right button visibility
     *
     * @param show Boolean
     */
    fun isShowRightBtn(show: Boolean) {
        if (show) getIvHeaderRightBtn()?.visibility = View.VISIBLE else getIvHeaderRightBtn()?.visibility = View.GONE
    }

    /**
     * set the header left button visibility
     *
     * @param show Boolean
     */
    fun isShowLeftBtn(show: Boolean) {
        if (show) getIvHeaderLeftBtn()?.visibility = View.VISIBLE else getIvHeaderLeftBtn()?.visibility = View.GONE
    }

    /**
     * set the double back confirm function
     */
    fun doubleBack() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce = true
        Toast.makeText(this@RYBaseActivity, resources.getString(R.string.global_back_again), Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    /**
     * Convert the root level of the fragment
     *
     * @param type Int?
     */
    fun switchRootFragment(type: Int?) {
        switchFragment(RYLibSetting.initRYFragmentModule?.initFragment(type!!)!!)
    }

    /**
     * Convert the child level of the fragment
     *
     * @param f RYBaseFragment
     */
    fun switchToDetailPage(f: RYBaseFragment) {
        supportFragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .replace(getRootFragmentId(), f)
            .commit()
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
