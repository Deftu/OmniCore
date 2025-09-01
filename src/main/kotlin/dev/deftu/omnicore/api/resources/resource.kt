@file:JvmName("OmniResource")

package dev.deftu.omnicore.api.resources

import com.google.gson.JsonElement
import dev.deftu.omnicore.api.serialization.OmniJson
import net.minecraft.resource.Resource
import java.io.Reader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

public fun Resource.readBytes(): ByteArray {
    return inputStream.readBytes()
}

public fun Resource.readString(charset: Charset = StandardCharsets.UTF_8): String {
    return inputStream.bufferedReader(charset).use(Reader::readText)
}

public fun Resource.readJson(): JsonElement {
    return OmniJson.parse(inputStream.bufferedReader())
}

public fun Resource.readJsonSafe(): JsonElement? {
    return OmniJson.parseSafe(inputStream.bufferedReader())
}
