package com.test

data class TestData(
    val name: String,
    val value: Int
) {
    override fun toString(): String {
        return "TestData(name='$name', value=$value)"
    }
}
