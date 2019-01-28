package com.ryanyu.basecore.utils

import android.app.ProgressDialog
import android.content.Context
import android.util.DisplayMetrics
import android.widget.Toast
import com.ryanyu.basecore.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * Created by Ryan Yu on 9/1/2019.
 */

object RYEasyFunction {
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

    /**
     * show loading dilaog
     *
     * @param ctx Context?
     */
    fun showProgressDialog(ctx: Context?) {
        dismissProgressDialog()
        pdRootLoadingDialog = ProgressDialog(ctx)
        pdRootLoadingDialog?.run {
            setMessage(ctx?.resources?.getString(R.string.global_loading))
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
    fun easyToast(ctx: Context?, message: String) {
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

}