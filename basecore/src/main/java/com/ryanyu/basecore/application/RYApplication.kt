package com.ryanyu.basecore.application

import android.app.Application
import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import com.ryanyu.basecore.provider.Constant
import com.ryanyu.basecore.provider.Constant.Companion.EN
import com.ryanyu.basecore.provider.Constant.Companion.SC
import com.ryanyu.basecore.provider.Constant.Companion.TC
import java.util.*

/**
 * Update 2019-01-29
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

open class RYApplication : Application() {
    var ctx: Context? = null
    var currentLanguage = Constant.EN
    init {
        instance = this
    }

    companion object {
        var instance: RYApplication? = null


        /**
         * get Application
         *
         * @return RYApplication?
         */
        fun getApplication(): RYApplication? {
            return instance
        }

        /**
         * get application context
         *
         * @return Context
         */
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext()
        initSharedPreference()
    }

    /**
     * for update language
     *
     * @param language Int
     */
    fun updateLanguage(language : Int){
        this.currentLanguage = language
        refreshLanguage(ctx)
    }

    private fun initSharedPreference() {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        currentLanguage = sp.getInt(Constant.SP_LANGUAGE_KEY, -1)
        if (currentLanguage == -1) {
            getDefaultLanguage()
        } else {
            refreshLanguage(ctx)
        }
    }

    fun getDefaultLanguage() {
        val configuration = resources.configuration

        if (configuration.locale.language == "zh") {
            if (configuration.locale.country == "TW" || configuration.locale.country == "HK") {
                currentLanguage = TC
            } else {
                currentLanguage = SC
            }
        } else {
            currentLanguage = EN
        }
        refreshLanguage(ctx)

    }

    fun refreshLanguage(context: Context?) {
        val localLocale: Locale
        when (currentLanguage) {
            SC -> localLocale = Locale.SIMPLIFIED_CHINESE
            TC -> localLocale = Locale.TRADITIONAL_CHINESE
            EN -> localLocale = Locale.ENGLISH
            else -> localLocale = Locale.ENGLISH
        }

        Locale.setDefault(localLocale)

        val localResources = context?.resources

        val localConfiguration = context?.resources
            ?.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            localConfiguration?.setLocale(localLocale)
        } else {
            localConfiguration?.locale = localLocale
        }

        localConfiguration?.locale = localLocale
        val localDisplayMetrics = localResources
            ?.displayMetrics
        localResources?.updateConfiguration(
            localConfiguration,
            localDisplayMetrics
        )

        val sp = PreferenceManager.getDefaultSharedPreferences(context)

        sp.edit().putInt(Constant.SP_LANGUAGE_KEY, currentLanguage).apply()

    }

}