package com.softsandr.codegen.exception

internal class ClassPackageNotFoundException(className: String) :
        Exception("The package of $className class has no name")