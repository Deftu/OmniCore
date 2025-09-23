package dev.deftu.omnicore.api.client.textures

public data class TextureConfiguration(
    val minFilter: TextureFilter = TextureFilter.LINEAR,
    val magFilter: TextureFilter = TextureFilter.LINEAR,
    val wrapS: TextureWrap = TextureWrap.CLAMP_TO_EDGE,
    val wrapT: TextureWrap = TextureWrap.CLAMP_TO_EDGE
) {
    public companion object {
        @JvmField public val DEFAULT: TextureConfiguration = TextureConfiguration()
    }

    public fun withMinFilter(min: TextureFilter): TextureConfiguration {
        return if (this.minFilter == min) this else copy(minFilter = min)
    }

    public fun withMagFilter(mag: TextureFilter): TextureConfiguration {
        return if (this.magFilter == mag) this else copy(magFilter = mag)
    }

    public fun withFilter(min: TextureFilter, mag: TextureFilter): TextureConfiguration {
        return if (this.minFilter == min && this.magFilter == mag) this else copy(minFilter = min, magFilter = mag)
    }

    public fun withWrapS(wrap: TextureWrap): TextureConfiguration {
        return if (this.wrapS == wrap) this else copy(wrapS = wrap)
    }

    public fun withWrapT(wrap: TextureWrap): TextureConfiguration {
        return if (this.wrapT == wrap) this else copy(wrapT = wrap)
    }

    public fun withWrap(s: TextureWrap, t: TextureWrap): TextureConfiguration {
        return if (this.wrapS == s && this.wrapT == t) this else copy(wrapS = s, wrapT = t)
    }
}
