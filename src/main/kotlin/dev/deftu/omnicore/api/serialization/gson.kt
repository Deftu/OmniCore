package dev.deftu.omnicore.api.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder

public val Gson.asBuilder: GsonBuilder
    get() = OmniJson.newGsonBuilder(this)

public val Gson.pretty: Gson
    get() = OmniJson.withPrettyPrinting(this)

public fun Gson.newGsonBuilder(): GsonBuilder {
    return OmniJson.newGsonBuilder(this)
}

public fun Gson.withPrettyPrinting(): Gson {
    return OmniJson.withPrettyPrinting(this)
}
