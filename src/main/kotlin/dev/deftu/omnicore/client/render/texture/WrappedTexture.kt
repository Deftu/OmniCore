package dev.deftu.omnicore.client.render.texture

public class WrappedTexture(
    override val id: Int,
    override val width: Int,
    override val height: Int,
    override val format: GpuTexture.TextureFormat
) : GlTexture(format) {

    public companion object {

        //#if MC >= 1.21.5
        //$$ @JvmStatic
        //$$ public fun from(vanillaTexture: com.mojang.blaze3d.opengl.GlTexture): WrappedTexture {
        //$$     return WrappedTexture(
        //$$         vanillaTexture.glId(),
        //$$         vanillaTexture.getWidth(0),
        //$$         vanillaTexture.getHeight(0),
        //$$         GpuTexture.TextureFormat.from(vanillaTexture.format)
        //$$     )
        //$$ }
        //#endif

        //#if MC >= 1.21.6
        //$$ @JvmStatic
        //$$ public fun from(vanillaTexture: net.minecraft.client.texture.GlTextureView): WrappedTexture {
        //$$     val texture = vanillaTexture.texture()
        //$$     return WrappedTexture(
        //$$         texture.glId,
        //$$         vanillaTexture.getWidth(0),
        //$$         vanillaTexture.getHeight(0),
        //$$         GpuTexture.TextureFormat.from(texture.format)
        //$$     )
        //$$ }
        //#endif

    }

    override fun resize(width: Int, height: Int) {
        throw UnsupportedOperationException("Cannot resize a wrapped texture")
    }

    override fun close() {
        throw UnsupportedOperationException("Cannot close a wrapped texture")
    }

}
