package com.magnum_ti1.plant.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var email: String? = null,
    var password: String? = null,
    var username: String? = null,
    var phoneNumber: String? = null,
    var userId: String? = null,
    var photo: String? = null
) : Parcelable
