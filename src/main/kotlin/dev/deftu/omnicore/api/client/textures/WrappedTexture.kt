package dev.deftu.omnicore.api.client.textures

public class WrappedTexture(
    override val id: Int,
    override val width: Int,
    override val height: Int,
    override val format: OmniTextureFormat
) : AbstractGlTexture(format) {
    override fun resize(width: Int, height: Int) {
        throw UnsupportedOperationException("Cannot resize a wrapped texture")
    }

    override fun close() {
        throw UnsupportedOperationException("Cannot close a wrapped texture")
    }
}
