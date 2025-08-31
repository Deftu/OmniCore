package dev.deftu.omnicore.api.resources

import com.google.gson.JsonElement
import dev.deftu.omnicore.api.serialization.OmniJson
import net.minecraft.resource.Resource
import java.io.Reader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

public object OmniResource {
    @JvmStatic
    public fun readBytes(resource: Resource): ByteArray {
        return resource.inputStream.readBytes()
    }

    @JvmStatic
    @JvmOverloads
    public fun readString(resource: Resource, charset: Charset = StandardCharsets.UTF_8): String {
        return resource.inputStream.bufferedReader(charset).use(Reader::readText)
    }

    @JvmStatic
    public fun readJson(resource: Resource): JsonElement {
        return OmniJson.parse(resource.inputStream.bufferedReader())
    }

    @JvmStatic
    public fun readJsonSafe(resource: Resource): JsonElement? {
        return OmniJson.parseSafe(resource.inputStream.bufferedReader())
    }
}
