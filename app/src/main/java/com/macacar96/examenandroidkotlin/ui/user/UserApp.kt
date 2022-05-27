package com.macacar96.examenandroidkotlin.ui.user

import android.app.Application
import androidx.room.Room

class UserApp : Application() {

    val room = Room
        .databaseBuilder(this, UserDatabase::class.java, "user")
        .build()
}