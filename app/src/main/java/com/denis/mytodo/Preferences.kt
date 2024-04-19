package com.denis.mytodo

import android.content.Context
import android.content.SharedPreferences

object Preferences {


    fun Context.isLoggedIn(): Boolean {
        return prefObj(this).getBoolean("IsLoggedIn", false)
    }

    fun Context.setLoggedIn(value: Boolean) {
        prefObj(this).edit().putBoolean("IsLoggedIn", value).apply()
    }


    private fun prefObj(context: Context):SharedPreferences{
        return context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)
    }


}