package com.softsandr.codegen.api

import android.content.Context
import android.util.Log
import android.widget.Toast

class ApiController(private val ctx: Context) {

    fun sayHello() {
        Log.d(ApiController::class.java.simpleName, "sayHello()")
        Toast.makeText(ctx, "Hello!!!", Toast.LENGTH_SHORT).show()
    }
}