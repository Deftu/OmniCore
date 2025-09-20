@file:JvmName("OmniJson")

package dev.deftu.omnicore.api.serialization

import com.google.gson.*
import java.io.Reader
import java.util.Optional

//#if MC <= 1.17.1
//$$ private val parser = JsonParser()
//#endif

@Throws(JsonSyntaxException::class)
public fun parseJsonOrThrow(json: String): JsonElement {
    //#if MC >= 1.18.2
    return JsonParser.parseString(json)
    //#else
    //$$ return parser.parse(json)
    //#endif
}

@Throws(JsonSyntaxException::class)
public fun parseJsonOrThrow(reader: Reader): JsonElement {
    //#if MC >= 1.18.2
    return JsonParser.parseReader(reader)
    //#else
    //$$ return parser.parse(reader)
    //#endif
}

public fun parseJsonOrNull(json: String): JsonElement? {
    return try {
        parseJsonOrThrow(json)
    } catch (e: JsonSyntaxException) {
        null
    }
}

public fun parseJsonOrNull(reader: Reader): JsonElement? {
    return try {
        parseJsonOrThrow(reader)
    } catch (e: JsonSyntaxException) {
        null
    }
}

public fun parseJson(json: String): Optional<JsonElement> {
    return Optional.ofNullable(parseJsonOrNull(json))
}

public fun parseJson(reader: Reader): Optional<JsonElement> {
    return Optional.ofNullable(parseJsonOrNull(reader))
}
