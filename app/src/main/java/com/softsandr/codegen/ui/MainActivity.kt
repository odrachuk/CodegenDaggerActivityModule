package com.softsandr.codegen.ui

import android.os.Bundle
import com.softsandr.codegen.R
import com.softsandr.codegen.annotation.GenerateActivityModule
import com.softsandr.codegen.di.InjectableActivity

@GenerateActivityModule(fragments = [MainActivityFragment::class])
class MainActivity : InjectableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        supportFragmentManager.beginTransaction().replace(R.id.activity_main__frame, MainActivityFragment()).commit()
    }
}
