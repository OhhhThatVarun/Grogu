package com.varun.grogu.data.extensions

fun String.capitalizeFirstChar(): String {
    return replaceFirstChar { it.uppercaseChar() }
}