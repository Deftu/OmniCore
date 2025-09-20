@file:JvmName("OmniResource")

package dev.deftu.omnicore.api.resources

import com.google.gson.JsonElement
import dev.deftu.omnicore.api.serialization.parseJson
import dev.deftu.omnicore.api.serialization.parseJsonOrNull
import dev.deftu.omnicore.api.serialization.parseJsonOrThrow
import net.minecraft.resource.Resource
import java.io.Reader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Optional

public fun Resource.readBytes(): ByteArray {
    return inputStream.readBytes()
}

public fun Resource.readString(charset: Charset = StandardCharsets.UTF_8): String {
    return inputStream.bufferedReader(charset).use(Reader::readText)
}

public fun Resource.readJson(): Optional<JsonElement> {
    return parseJson(inputStream.bufferedReader())
}

public fun Resource.readJsonOrThrow(): JsonElement {
    return parseJsonOrThrow(inputStream.bufferedReader())
}

public fun Resource.readJsonOrNull(): JsonElement? {
    return parseJsonOrNull(inputStream.bufferedReader())
}
