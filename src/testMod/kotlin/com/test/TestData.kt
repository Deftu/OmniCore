package com.test

import com.google.gson.Gson
import dev.deftu.omnicore.common.OmniJson
import java.io.InputStream

data class TestData(
    val name: String,
    val value: Int
) {
    companion object {
        private val gson = Gson()

        @JvmField
        val DEFAULT = TestData("default", 0)

        fun fromJson(inputStream: InputStream): TestData? {
            val json = OmniJson.parseSafe(inputStream.reader()) ?: return null
            return try {
                gson.fromJson(json, TestData::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    override fun toString(): String {
        return "TestData(name='$name', value=$value)"
    }
}
