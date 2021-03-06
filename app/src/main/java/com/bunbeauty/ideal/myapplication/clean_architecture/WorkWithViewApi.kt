package com.bunbeauty.ideal.myapplication.clean_architecture

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlin.math.pow
import kotlin.math.sqrt

class WorkWithViewApi {
    companion object {
        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }

        fun getInches(context: Context): Double {
            val dm = DisplayMetrics()

            val windowManager = context
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager

            windowManager.defaultDisplay.getMetrics(dm)
            val width = dm.widthPixels
            val height = dm.heightPixels
            val wi = width.toDouble() / dm.xdpi.toDouble()
            val hi = height.toDouble() / dm.ydpi.toDouble()
            val x = wi.pow(2.0)
            val y = hi.pow(2.0)
            return sqrt(x + y) + 0.3
        }
    }

}
