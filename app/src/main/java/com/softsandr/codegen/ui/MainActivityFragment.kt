package com.softsandr.codegen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.softsandr.codegen.R
import com.softsandr.codegen.annotation.GenerateFragmentModule
import com.softsandr.codegen.api.ApiController
import com.softsandr.codegen.di.InjectableFragment
import javax.inject.Inject

@GenerateFragmentModule
class MainActivityFragment : InjectableFragment() {

    @Inject
    lateinit var apiController: ApiController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_main_fragment, container, false)
        view.findViewById<View>(R.id.activity_main_fragment__btn).setOnClickListener { apiController.sayHello() }
        return view
    }
}