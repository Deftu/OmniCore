package dev.deftu.omnicore.internal.client.textures

import dev.deftu.omnicore.api.client.textures.OmniTextureHandle
import net.minecraft.client.renderer.texture.AbstractTexture
import org.jetbrains.annotations.ApiStatus

//#if MC >= 1.21.5
//#if MC >= 1.21.6
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.GpuTextureView
//#endif

import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.textures.TextureFormat
import com.mojang.blaze3d.opengl.GlTexture
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
//#endif

//#if MC <= 1.21.3
//$$ import net.minecraft.server.packs.resources.ResourceManager
//#endif

@ApiStatus.Internal
public class HandleBackedTexture(public val handle: OmniTextureHandle) : AbstractTexture() {
    private companion object {
        //#if MC >= 1.21.5
        // Cache the constructor lookup; doing this on every call is slow and brittle.
        private val CTOR: Lazy<MethodHandle> = lazy(LazyThreadSafetyMode.PUBLICATION) {
            val mt = MethodType.methodType(
                Void.TYPE,
                //#if MC >= 1.21.6
                Int::class.java, // usage flags
                //#endif
                String::class.java, // debug name
                TextureFormat::class.java, // format
                Int::class.java, // width
                Int::class.java, // height
                //#if MC >= 1.21.6
                Int::class.java, // depth/layers
                //#endif
                Int::class.java, // mips
                Int::class.java  // GL ID
            )

            // Use a private lookup to survive non-public constructors.
            val lookup = MethodHandles.lookup()
            try {
                MethodHandles.privateLookupIn(GlTexture::class.java, lookup).findConstructor(GlTexture::class.java, mt)
            } catch (e: Throwable) {
                // Fall back to a regular lookup; if that fails too, surface a clear error.
                try {
                    lookup.findConstructor(GlTexture::class.java, mt)
                } catch (e2: Throwable) {
                    throw IllegalStateException("Failed to access GlTexture constructor; mappings/descriptor may have changed", e2)
                }
            }
        }
        //#endif
    }

    @Volatile private var isInitialized = false

    private fun initialize() {
        if (isInitialized) {
            return
        }

        synchronized(this) {
            if (isInitialized) {
                return
            }

            //#if MC >= 1.21.5
            val ctor = CTOR.value
            //#if MC >= 1.21.6
            val usage = GpuTexture.USAGE_TEXTURE_BINDING or
                    GpuTexture.USAGE_COPY_SRC or
                    GpuTexture.USAGE_COPY_DST
            //#endif
            val glTexture = ctor.invoke(
                //#if MC >= 1.21.6
                usage,
                //#endif
                "OmniTextureHandle ${handle.id}",
                handle.format.vanilla,
                handle.width,
                handle.height,
                //#if MC >= 1.21.6
                1,
                //#endif
                1,
                handle.id
            ) as GlTexture

            glTexture.setTextureFilter(FilterMode.NEAREST, true)
            this.texture = glTexture
            println("Initialized GlTexture for OmniTextureHandle ${handle.id}: $glTexture")
            //#if MC >= 1.21.6
            this.textureView = RenderSystem.getDevice().createTextureView(this.texture!!)
            println("Initialized GlTextureView for OmniTextureHandle ${handle.id}: $textureView")
            //#endif
            //#else
            //$$ this.id = handle.id
            //#endif

            isInitialized = true
        }
    }

    //#if MC >= 1.21.5
    //#if MC >= 1.21.6
    override fun getTextureView(): GpuTextureView {
        initialize()
        return super.getTextureView()
    }

    override fun setUseMipmaps(mipmaps: Boolean) {
        initialize()
        super.setUseMipmaps(mipmaps)
    }
    //#endif

    override fun setClamp(clamp: Boolean) {
        initialize()
        super.setClamp(clamp)
    }

    override fun setFilter(bilinear: Boolean, mipmap: Boolean) {
        initialize()
        super.setFilter(bilinear, mipmap)
    }

    override fun getTexture(): GpuTexture {
        initialize()
        return super.getTexture()
    }

    override fun close() {
        try {
            super.close()
        } finally {
            isInitialized = false
        }
    }
    //#else
    //#if MC <= 1.21.3
    //$$ override fun load(resourceManager: ResourceManager?) {
    //$$     initialize()
    //$$ }
    //#endif
    //$$
    //$$ override fun getId(): Int {
    //$$     initialize()
    //$$     return super.getId()
    //$$ }
    //$$
    //#if MC >= 1.16.5
    //$$ override fun close() {
    //$$     try {
    //$$         super.close()
    //$$     } finally {
    //$$         isInitialized = false
    //$$     }
    //$$ }
    //#endif
    //#endif
}
