package com.magnum_ti1.plant.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.magnum_ti1.plant.common.Constant.PREF_EMAIL
import com.magnum_ti1.plant.common.Constant.PREF_PHONE_NUMBER
import com.magnum_ti1.plant.common.Constant.PREF_PHOTO
import com.magnum_ti1.plant.common.Constant.PREF_USER_ID
import com.magnum_ti1.plant.common.Constant.PREF_USER_NAME
import com.magnum_ti1.plant.data.entity.User

class UserPreferences {
    var userInfo: User
        get() {
            val user = User()
            user.username = preferences.getString(PREF_USER_NAME, "")
            user.email = preferences.getString(PREF_EMAIL, "")
            user.userId = preferences.getString(PREF_USER_ID, "")
            user.photo = preferences.getString(PREF_PHOTO, "")
            user.phoneNumber = preferences.getString(PREF_PHONE_NUMBER, "")
            return user
        }
        set(modelUser) {
            val editor = preferences.edit()
            editor.putString(PREF_USER_NAME, modelUser.username)
            editor.putString(PREF_EMAIL, modelUser.email)
            editor.putString(PREF_USER_ID, modelUser.userId)
            editor.putString(PREF_PHOTO, modelUser.photo)
            editor.putString(PREF_PHONE_NUMBER, modelUser.phoneNumber)
            editor.apply()
        }

    companion object {
        private lateinit var preferences: SharedPreferences

        fun initPreferences(context: Context): UserPreferences {
            preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE)
            return UserPreferences()
        }
    }
}