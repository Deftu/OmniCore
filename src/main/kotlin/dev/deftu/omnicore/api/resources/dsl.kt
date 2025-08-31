package dev.deftu.omnicore.api.resources

import com.google.gson.JsonElement
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.Optional

public fun ResourceManager.findFirst(identifier: Identifier): Optional<Resource> {
    return OmniResourceManager.findFirst(this, identifier)
}

public fun ResourceManager.findFirstOrThrow(identifier: Identifier): Resource {
    return OmniResourceManager.findFirstOrThrow(this, identifier)
}

public fun ResourceManager.findFirstOrNull(identifier: Identifier): Resource? {
    return OmniResourceManager.findFirstOrNull(this, identifier)
}

public fun ResourceManager.findAll(identifier: Identifier): List<Resource> {
    return OmniResourceManager.findAll(this, identifier)
}

public fun ResourceManager.findSequence(identifier: Identifier): Sequence<Resource> {
    return OmniResourceManager.findSequence(this, identifier)
}

public fun ResourceManager.exists(identifier: Identifier): Boolean {
    return OmniResourceManager.exists(this, identifier)
}

public fun Resource.readBytes(): ByteArray {
    return OmniResource.readBytes(this)
}

public fun Resource.readString(charset: Charset = StandardCharsets.UTF_8): String {
    return OmniResource.readString(this, charset)
}

public fun Resource.readJson(): JsonElement {
    return OmniResource.readJson(this)
}

public fun Resource.readJsonSafe(): JsonElement? {
    return OmniResource.readJsonSafe(this)
}
