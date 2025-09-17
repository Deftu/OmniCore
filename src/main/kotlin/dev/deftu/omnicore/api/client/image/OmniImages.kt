package dev.deftu.omnicore.api.client.image

import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import dev.deftu.omnicore.api.client.textures.OmniTextures
import dev.deftu.omnicore.api.resources.findFirstOrNull
import dev.deftu.omnicore.api.resources.readBytes
import dev.deftu.omnicore.internal.client.image.OmniImageImpl
import dev.deftu.omnicore.internal.client.image.ImageInternals
import net.minecraft.util.Identifier
import java.io.File
import java.io.InputStream
import java.nio.file.Path

//#if MC >= 1.21.6
import net.minecraft.client.texture.GlTextureView
//#endif

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
//#endif

//#if MC >= 1.16.5
import net.minecraft.client.texture.NativeImage
import java.io.ByteArrayInputStream
import java.nio.file.Files
//#else
//$$ import java.awt.image.BufferedImage
//$$ import javax.imageio.ImageIO
//#endif

public object OmniImages {
    @JvmStatic
    public fun create(width: Int, height: Int): OmniImage {
        return OmniImageImpl(width, height)
    }

    @JvmStatic
    public fun from(
        //#if MC >= 1.16.5
        native: NativeImage,
        //#else
        //$$ native: BufferedImage,
        //#endif
    ): OmniImage {
        return OmniImageImpl(native.width, native.height).apply {
            //#if MC >= 1.16.5
            this.native.copyFrom(native)
            //#else
            //$$ val graphics = this.native.createGraphics()
            //$$ graphics.drawImage(native, 0, 0, null)
            //$$ graphics.dispose()
            //#endif
        }
    }

    @JvmStatic
    public fun from(texture: OmniTextureHandle): OmniImage {
        val image = OmniImageImpl(texture.width, texture.height)
        texture.using {
            ImageInternals.loadTextureInto(texture, image)
        }

        return image
    }

    //#if MC >= 1.21.5
    @JvmStatic
    public fun wrap(vanillaTexture: GlTexture): OmniImage {
        return from(OmniTextures.wrap(vanillaTexture))
    }
    //#endif

    //#if MC >= 1.21.6
    @JvmStatic
    public fun wrap(vanillaTexture: GlTextureView): OmniImage {
        return from(OmniTextures.wrap(vanillaTexture))
    }
    //#endif

    @JvmStatic
    public fun read(inputStream: InputStream): OmniImage {
        //#if MC >= 1.16.5
        return from(NativeImage.read(inputStream))
        //#else
        //$$ return from(ImageIO.read(inputStream))
        //#endif
    }

    @JvmStatic
    public fun read(path: Path): OmniImage {
        //#if MC >= 1.16.5
        val data = Files.readAllBytes(path)
        val inputStream = ByteArrayInputStream(data)
        return read(inputStream)
        //#else
        //$$ return from(ImageIO.read(path.toFile()))
        //#endif
    }

    @JvmStatic
    public fun read(file: File): OmniImage {
        //#if MC >= 1.16.5
        val data = file.readBytes()
        val inputStream = ByteArrayInputStream(data)
        return read(inputStream)
        //#else
        //$$ return from(ImageIO.read(file))
        //#endif
    }

    @JvmStatic
    public fun resource(location: Identifier): OmniImage? {
        val inputStream = client.resourceManager.findFirstOrNull(location)
            ?.readBytes()
            ?.let(::ByteArrayInputStream)
            ?: return null
        return read(inputStream)
    }
}
