package com.risingsound.kotlinapp

import android.app.Application

class AppContext : Application() {
    var userType: String? = null
    var consentNotification: Boolean? = false
}
