package com.ryanyu.basecore.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.ryanyu.basecore.observer.RYEasyObserver
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import android.support.v4.os.HandlerCompat.postDelayed
import com.ryanyu.basecore.listener.RYWaitListener


/**
 * Created by Ryan Yu on 9/1/2019.
 */

object RYBox {
    private var pdRootLoadingDialog: ProgressDialog? = null

    fun <A, B> setAPIValue(value1: A, value2: B): ArrayList<Any> {
        var array: ArrayList<Any> = ArrayList()
        array.add(value1 as Any)
        array.add(value2 as Any)
        return array
    }

    fun <A, B, C> setAPIValue(value1: A, value2: B, value3: C): ArrayList<Any> {
        var array: ArrayList<Any> = ArrayList()
        array.add(value1 as Any)
        array.add(value2 as Any)
        array.add(value3 as Any)
        return array
    }

    fun <A, B, C, D> setAPIValue(value1: A, value2: B, value3: C, value4: D): ArrayList<Any> {
        var array: ArrayList<Any> = ArrayList()
        array.add(value1 as Any)
        array.add(value2 as Any)
        array.add(value3 as Any)
        array.add(value4 as Any)
        return array
    }

    fun <A, B, C, D, E> setAPIValue(value1: A, value2: B, value3: C, value4: D, value5: E): ArrayList<Any> {
        var array: ArrayList<Any> = ArrayList()
        array.add(value1 as Any)
        array.add(value2 as Any)
        array.add(value3 as Any)
        array.add(value4 as Any)
        array.add(value5 as Any)
        return array
    }

    fun getId(resName: String, c: Class<*>): Int {
        try {
            val idField = c.getDeclaredField(resName)
            return idField.getInt(idField)
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }

    fun getDrawable(ctx: Context, resName: String): Int {
        val id = ctx.getResources().getIdentifier(resName, "drawable", ctx.getPackageName())
        return id
    }


    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun easyText(ctx: Context?, stringKey: Int?): String {
        return stringKey?.let { ctx?.resources?.getString(it) } ?: ""
    }


    fun getCorrectDateFormat(date: String, fromDateFormat: String, toDateFormat: String): String {
        val newDateFormat = SimpleDateFormat(toDateFormat)
        val oldDateFormat = SimpleDateFormat(fromDateFormat)
        try {
            val DateD = oldDateFormat.parse(date)
            return newDateFormat.format(DateD)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newDateFormat.format(date)
    }


    fun getCorrectDateFormatReturnDate(date: String, fromDateFormat: String, toDateFormat: String): Date? {
        val newDateFormat = SimpleDateFormat(toDateFormat)
        val oldDateFormat = SimpleDateFormat(fromDateFormat)
        try {
            val DateD = oldDateFormat.parse(date)
            var newDateString = newDateFormat.format(DateD)
            return newDateFormat.parse(newDateString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    val TAG = "RYANLog"

    fun Log(value: String) {
        android.util.Log.d(TAG, value)
    }

    fun systemLog(value: Any) {
        System.out.println("$TAG $value")
    }

    /**
     * show loading dilaog
     *
     * @param ctx Context?
     */
    fun showProgressDialog(ctx: Context?) {
        dismissProgressDialog()
        pdRootLoadingDialog = ProgressDialog(ctx)
        pdRootLoadingDialog?.run {
            setMessage(ctx?.resources?.getString(com.ryanyu.basecore.R.string.global_loading))
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setCancelable(false)
            pdRootLoadingDialog?.show()
        }
    }

    /**
     * dissmiss loading dialog
     */
    fun dismissProgressDialog() {
        pdRootLoadingDialog?.let { if (it.isShowing) it.dismiss() }
        pdRootLoadingDialog = null
    }

    /**
     * easy to show toast message
     *
     * @param message String
     */
    fun easyToast(ctx: Context?, message: String?) {
        Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * check is valid email format
     *
     * @param email String
     * @return Boolean
     */
    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    /**
     * check is valid phone number
     *
     * @param phoneNo String
     * @return Boolean
     */
    fun isPhoneNumberValid(phoneNo: String): Boolean {
        //validate phone numbers of format "1234567890"
        return if (phoneNo.matches("\\d{10}".toRegex()))
            true
        else if (phoneNo.matches("\\d{9}".toRegex()))
            true
        else if (phoneNo.matches("\\d{8}".toRegex()))
            true
        else if (phoneNo.matches("\\d{11}".toRegex()))
            true
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}".toRegex()))
            true
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}".toRegex()))
            true
        else phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}".toRegex())
    }

    /**
     * check Is not space string
     *
     * @param target String?
     * @return Boolean
     */
    fun checkStringIsExist(target: String?): Boolean {
        return target != null && target != ""
    }

    /**
     * dp to px
     *
     * @param ctx Context
     * @param dp Float
     * @return Int
     */
    fun convertDpToPixel(ctx: Context, dp: Float): Int {
        val resources = ctx.resources
        val metrics = resources.displayMetrics
        val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        return Math.round(px)
    }


    /**
     * check now date is between start date and end date
     *
     * @param startDate String
     * @param endDate String
     * @param dateFormat String
     * @return Boolean
     */
    fun nowDateIsBetweenRange(startDate: String, endDate: String, dateFormat: String): Boolean {
        val nowDate = Calendar.getInstance().time

        val sdf = SimpleDateFormat(dateFormat)
        try {
            val startDate = sdf.parse(startDate)
            val endDate = sdf.parse(endDate)
            return endDate.time >= nowDate.time && nowDate.time >= startDate.time

        } catch (ee: ParseException) {
            ee.printStackTrace()
        }

        return false
    }

    fun <T> ryWait(sec: Long, function: () -> T) {
        val handler = Handler()
        handler.postDelayed({
            function()
        }, sec * 1000)
    }


    fun <T> ryWait(sec: Long, value: T, function: (value: T) -> Unit) {
        val handler = Handler()
        handler.postDelayed({
            function(value)
        }, sec * 1000)
    }

    fun ryWait(sec: Long, listener: RYWaitListener) {
        val handler = Handler()
        handler.postDelayed({
            listener.onTimeIsUp()
        }, sec * 1000)
    }


}