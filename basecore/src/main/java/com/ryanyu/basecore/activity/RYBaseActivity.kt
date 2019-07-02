package com.ryanyu.basecore.activity

import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.support.v4.app.FragmentActivity

import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.epiccomm.fsee.ryanlib.RYBaseFragment
import com.ryanyu.basecore.R
import com.ryanyu.basecore.utils.RYBox
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
    var doubleBackToExitPressedOnce = false
    var fragmentActivityResultObserver: Observer<ArrayList<Any>>? = null
    var inRootPage = true
    /**
     * set the double back confirm function
     */

    fun doubleBack(rootPageKillApp : Boolean = true) {
        if (doubleBackToExitPressedOnce || !inRootPage) {
            super.onBackPressed()
            return
        }

        if(rootPageKillApp) {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this@RYBaseActivity, resources.getString(R.string.global_back_again), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }else{
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
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
        fun getDlContentDrawer(): DrawerLayout
        fun isLinkToMenuBtn(): Boolean
    }

    fun closeDrawerLayout() {
        ryHeaderMenuDrawerMenu?.getDlContentDrawer()?.run {
            if (isDrawerOpen(GravityCompat.START)) {
                closeDrawer(GravityCompat.START)
            }
        }
    }

    fun islockDrawerLayout(isLockDrawer: Boolean) {
        this.isLockDrawer = true
        if (this.isLockDrawer) ryHeaderMenuDrawerMenu?.getDlContentDrawer()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED) else ryHeaderMenuDrawerMenu?.getDlContentDrawer()?.setDrawerLockMode(
            DrawerLayout.LOCK_MODE_UNLOCKED
        )
    }

    fun toggleDrawerLayout() {
        ryHeaderMenuDrawerMenu?.getDlContentDrawer()?.run {
            if (isDrawerOpen(GravityCompat.START)) closeDrawer(GravityCompat.START) else openDrawer(GravityCompat.START)
        }
    }

    /* ----------------------- START ----------------------- */

    var ryBaseFragmentCore: RYBaseFragmentCore? = null

    interface RYBaseFragmentCore {
        fun getRootFragmentId(): Int
        fun getFragment(fragmentType: Int): RYBaseFragment?
    }

    fun setRYBaseFragmentCore(ryBaseFragmentCore: RYBaseFragmentCore?) {
        this.ryBaseFragmentCore = ryBaseFragmentCore
    }

    /**
     * find fragment of now show
     *
     * @return RYBaseFragment?
     */
    fun getNowDisplayFragment(): RYBaseFragment? {
        return if (ryBaseFragmentCore?.getRootFragmentId() == 99) null else supportFragmentManager.findFragmentById(ryBaseFragmentCore?.getRootFragmentId()!!) as? RYBaseFragment
    }

    private fun switchFragment(f: RYBaseFragment) {
        closeDrawerLayout()
        if (f.getFragmentType() === getNowDisplayFragment()?.getFragmentType()) return

        for (i in 0 until supportFragmentManager.backStackEntryCount) {
            supportFragmentManager.popBackStackImmediate(null, android.support.v4.app.FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        val fragmentTransaction =
            supportFragmentManager.beginTransaction().setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.replace(ryBaseFragmentCore?.getRootFragmentId()!!, f).commitAllowingStateLoss()
    }

    /**
     * Convert the root level of the fragment
     *
     * @param type Int?
     */
    fun switchRootFragment(type: Int?) {
        RYBox.hideKeyboard(this)
        switchFragment(ryBaseFragmentCore?.getFragment(type!!)!!)
    }

    /**
     * Convert the child level of the fragment
     *
     * @param f RYBaseFragment
     */
    fun switchToDetailPage(f: RYBaseFragment) {
        RYBox.hideKeyboard(this)
        supportFragmentManager.beginTransaction().setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .replace(ryBaseFragmentCore?.getRootFragmentId()!!, f)
            .commit()
    }

    /**
     * add the child level of the fragment
     *
     * @param f RYBaseFragment
     */
    fun switchToAddDetailPage(f: RYBaseFragment) {
        RYBox.hideKeyboard(this)
        supportFragmentManager.beginTransaction().setTransition(android.support.v4.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
            .add(ryBaseFragmentCore?.getRootFragmentId()!!, f)
            .commit()
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
            RYBox.hideKeyboard(this)
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
     * Activity with header Control
     **/


    private var stopAutoBack = false
    var ryBaseHeader: RYBaseHeader? = null


    interface RYBaseHeader {
        fun getHeader(): View?
        fun getHeaderLeftBtn(): View?
        fun getHeaderRightBtn(): View?
        fun getHeaderBackBtn(): View?
        fun getHeaderTitleTextView(): TextView?
        fun isAutoHandleBackBtnDisplay(): Boolean
    }

    fun setRYBaseHeader(ryBaseHeader: RYBaseHeader) {
        this.ryBaseHeader = ryBaseHeader
    }

    /**
     * Can stop/start the base core control BackStack
     *
     * @param stopAutoBack Boolean
     */
    fun setAutoHandleBackBtnDisplay(stopAutoBack: Boolean) {
        this.stopAutoBack = stopAutoBack
    }

    /**
     * init base core
     *
     * !!!!!!must call!!!!
     */
    fun initRoot() {
        initBackButton()
        initAutoBack()
    }

    /**
     * set the header title
     *
     * @param title String?
     */
    fun setHeaderTitle(title: String?) {
        ryBaseHeader?.getHeaderTitleTextView()?.text = title
    }


    /**
     * set the headerview visibility
     *
     * @param show Boolean?
     */
    fun isShowHeaderView(show: Boolean) {
        if (show) ryBaseHeader?.getHeader()?.visibility = View.VISIBLE else ryBaseHeader?.getHeader()?.visibility =
            View.GONE
    }

    /**
     * set the header right button visibility
     *
     * @param show Boolean
     */
    fun isShowRightBtn(show: Boolean) {
        if (show) ryBaseHeader?.getHeaderRightBtn()?.visibility = View.VISIBLE else ryBaseHeader?.getHeaderRightBtn()
            ?.visibility = View.GONE
    }

    /**
     * set the header left button visibility
     *
     * @param show Boolean
     */
    fun isShowLeftBtn(show: Boolean) {
        if (show) ryBaseHeader?.getHeaderLeftBtn()?.visibility = View.VISIBLE else ryBaseHeader?.getHeaderLeftBtn()
            ?.visibility = View.GONE
    }

    private fun initBackButton() {
        ryBaseHeader?.getHeaderBackBtn()?.setOnClickListener {
            RYBox.hideKeyboard(this)
            if (!stopAutoBack) onBackPressed()

        }
    }

    private fun initAutoBack() {
        supportFragmentManager.addOnBackStackChangedListener {
            RYBox.hideKeyboard(this)
            if (supportFragmentManager.backStackEntryCount > 0) {
                inRootPage = false

                ryBaseHeader?.let{
                    if (it.isAutoHandleBackBtnDisplay()!! && !stopAutoBack) {
                        it.getHeaderBackBtn()?.visibility = View.VISIBLE
                    }
                }

                ryHeaderMenuDrawerMenu?.let{
                    if (!isLockDrawer) {
                        it.getDlContentDrawer()
                            ?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    }
                }

                ryBaseTabBar?.let{
                    if (it.isAutoHandleTabBarDisplay()!!) {
                        isShowTabBar(false)
                    }
                }

            } else {
                inRootPage = true

                ryBaseHeader?.let{
                    it.getHeaderBackBtn()?.visibility = View.GONE
                }

                ryHeaderMenuDrawerMenu?.let{
                    it.getDlContentDrawer()?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

                }
                isShowTabBar(true)
            }
        }
    }


/* ----------------------- END ----------------------- */


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
        fun isAutoHandleTabBarDisplay(): Boolean?
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
        ryBaseTabBar?.let{
            if (show) ryBaseTabBar?.getTabBar()?.visibility = View.VISIBLE else ryBaseTabBar?.getTabBar()?.visibility =
                View.GONE
        }

    }

    fun setRYBaseTabBar(ryBaseTabBar: RYBaseTabBar) {
        this.ryBaseTabBar = ryBaseTabBar
        for (i in 0 until ryBaseTabBar?.getTabBarBtn()?.size!!) {
            this.ryBaseTabBar?.getTabBarBtn()?.get(i)?.setOnClickListener {
                RYBox.hideKeyboard(this)
                tabBarOnClickListener(i)
            }
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

    fun setRYTabBarBtnNumberVisibility(tabNumber: Int, onOff: Int) {
        ryTabBarBtnNumber?.getTabBarBtnNumberTv()?.get(tabNumber)?.visibility = onOff
    }

    fun setRYTabBarBtnNumber(tabNumber: Int, num: String) {
        ryTabBarBtnNumber?.getTabBarBtnNumberTv()?.get(tabNumber)?.text = num
    }

    fun setRYTabBarBtnNumberWithZeroCheck(tabNumber: Int, num: String) {
        if (num.equals("0")) {
            setRYTabBarBtnNumberVisibility(tabNumber, View.GONE)
            ryTabBarBtnNumber?.getTabBarBtnNumberTv()?.get(tabNumber)?.text = num
        } else {
            setRYTabBarBtnNumberVisibility(tabNumber, View.VISIBLE)
            ryTabBarBtnNumber?.getTabBarBtnNumberTv()?.get(tabNumber)?.text = num
        }
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
