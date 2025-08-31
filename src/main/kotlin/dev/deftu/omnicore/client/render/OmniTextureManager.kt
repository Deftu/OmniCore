@file:Suppress("MemberVisibilityCanBePrivate")

package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.api.annotations.GameSide
import dev.deftu.omnicore.api.annotations.Side
import dev.deftu.omnicore.client.OmniClient
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.texture.TextureManager
import net.minecraft.util.Identifier
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.imageio.ImageIO

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
import com.mojang.blaze3d.textures.TextureFormat
//#endif

//#if MC >= 1.21.4
import dev.deftu.omnicore.common.OmniIdentifier
//#endif

//#if MC <= 1.16.5
//$$ import net.minecraft.client.texture.ResourceTexture
//#endif

//#if MC >= 1.16.5 && MC <= 1.21.4
//$$ import com.mojang.blaze3d.platform.TextureUtil
//#endif

//#if MC >= 1.16.5
import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.texture.NativeImage
//#if FORGE
//$$ import net.minecraft.client.renderer.texture.*
//#else
//#endif
//#else
//#if FORGE
//$$ import net.minecraft.client.renderer.texture.*
//#else
//$$ import net.minecraft.client.texture.*
//#endif
//#endif

@GameSide(Side.CLIENT)
public class OmniTextureManager private constructor(
    private val textureManager: TextureManager
) {

    public companion object {

        @JvmStatic
        @GameSide(Side.CLIENT)
        public val INSTANCE: OmniTextureManager by lazy {
            OmniTextureManager(get())
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun get(): TextureManager = OmniClient.getInstance().textureManager

    }

    //#if MC >= 1.21.5
    public class VanillaWrappedGlTexture(id: Int) : GlTexture(
    //#if MC >= 1.21.6
        USAGE_TEXTURE_BINDING,
    //#endif
        "",
        TextureFormat.RGBA8,
        0,
        0,
        0,
    //#if MC >= 1.21.6
        1,
    //#endif
        id
    ) {

        init {
            this.needsReinit = false
        }

    }
    //#endif

    @GameSide(Side.CLIENT)
    public fun getTexture(path: Identifier):
            //#if MC >= 1.16.5
            AbstractTexture?
            //#else
            //$$ ITextureObject?
            //#endif
    {
        return textureManager.getTexture(path)
    }
    
    @GameSide(Side.CLIENT)
    public fun isTextureLoaded(path: Identifier): Boolean {
        return getTexture(path) != null
    }

    @GameSide(Side.CLIENT)
    public fun getReleasedDynamicTexture(stream: InputStream): ReleasedDynamicTexture {
        try {
            //#if MC >= 1.14
            val image = NativeImage.read(stream)
            //#else
            //$$ val image = ImageIO.read(stream)
            //#endif
            return ReleasedDynamicTexture(OmniImage.from(image))
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    @GameSide(Side.CLIENT)
    public fun bindTexture(
        //#if MC >= 1.16.5
        texture: AbstractTexture
        //#else
        //$$ texture: ITextureObject
        //#endif
    ): OmniTextureManager = apply {
        //#if MC >= 1.21.5
        val presentedTexture =
            //#if MC >= 1.21.6
            RenderSystem.getDevice().createTextureView(texture.glTexture)
            //#else
            //$$ texture.glTexture
            //#endif
        RenderSystem.getDevice().createCommandEncoder().presentTexture(presentedTexture)
        //#elseif MC >= 1.16.5
        //$$ texture.bind()
        //#else
        //$$ bindTexture(texture.glTextureId)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun bindTexture(path: Identifier): OmniTextureManager = apply {
        val texture = getTexture(path) ?: return@apply
        bindTexture(texture)
    }

    @GameSide(Side.CLIENT)
    public fun bindTexture(index: Int, path: Identifier): OmniTextureManager = apply {
        bindTexture(index, getOrLoadId(path))
    }

    //#if MC >= 1.14
    @GameSide(Side.CLIENT)
    public fun registerTexture(path: Identifier, texture: AbstractTexture): OmniTextureManager = apply {
    //#else
    //$$ @GameSide(Side.CLIENT)
    //$$ public fun registerTexture(path: ResourceLocation, texture: ITextureObject): OmniTextureManager = apply {
    //#endif
        textureManager.registerTexture(path, texture)
    }

    @GameSide(Side.CLIENT)
    public fun registerImageTexture(path: Identifier, texture: BufferedImage): OmniTextureManager = apply {
        val stream = ByteArrayOutputStream()
        ImageIO.write(texture, "png", stream)
        OmniClient.execute {
            registerTexture(path, getReleasedDynamicTexture(stream.toByteArray().inputStream()))
        }
    }

    @GameSide(Side.CLIENT)
    public fun registerDynamicTexture(path: String, texture: NativeImageBackedTexture): OmniTextureManager = apply {
        //#if MC >= 1.21.4
        textureManager.registerTexture(OmniIdentifier.create(path), texture)
        //#else
        //$$ textureManager.registerDynamicTexture(path, texture)
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun destroyTexture(path: Identifier): OmniTextureManager = apply {
        textureManager.destroyTexture(path)
    }

    @GameSide(Side.CLIENT)
    public fun deleteTexture(id: Int): OmniTextureManager = apply {
        OmniTextureManager.deleteTexture(id)
    }

    @GameSide(Side.CLIENT)
    public fun getId(
        //#if MC >= 1.16.5
        texture: AbstractTexture
        //#else
        //$$ texture: ITextureObject
        //#endif
    ): Int {
        //#if MC >= 1.21.5
        return (texture.glTexture as GlTexture).glId
        //#elseif MC >= 1.17.1
        //$$ return texture.id
        //#else
        //$$ return texture.glId
        //#endif
    }

    @GameSide(Side.CLIENT)
    public fun getOrLoadId(identifier: Identifier): Int {
        val texture = textureManager.getTexture(identifier)
        //#if MC >= 1.17.1
        return getId(texture)
        //#else
        //$$ return if (texture != null) {
        //$$     getId(texture)
        //$$ } else {
        //$$     val newTexture = ResourceTexture(identifier)
        //$$     textureManager.registerTexture(identifier, newTexture)
        //$$     getId(newTexture)
        //$$ }
        //#endif
    }

}
