package dev.deftu.omnicore.client.render

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.client.texture.AbstractTexture
import java.io.Closeable
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.util.*
import java.util.concurrent.ConcurrentHashMap

//#if MC >= 1.21.6
import com.mojang.blaze3d.textures.GpuTextureView
//#endif

//#if MC >= 1.21.5
import net.minecraft.client.texture.GlTexture
import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.textures.FilterMode
import com.mojang.blaze3d.textures.GpuTexture
import com.mojang.blaze3d.textures.TextureFormat
//#endif

//#if MC <= 1.21.4
//$$ import dev.deftu.omnicore.client.OmniClient
//#endif

//#if MC <= 1.21.3
//$$ import net.minecraft.resource.ResourceManager
//#endif

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
@GameSide(Side.CLIENT)
public class ReleasedDynamicTexture(
    public val width: Int,
    public val height: Int,
    data: OmniImage?
) : AbstractTexture() {

    public constructor(width: Int, height: Int) : this(width, height, null)
    public constructor(image: OmniImage) : this(image.width, image.height, image)

    private var resources = Resources(this)
    private var data by resources::data

    init {
        resources.data = data ?: OmniImage(width, height)
    }

    @GameSide(Side.CLIENT)
    public var isUploaded: Boolean = false

    @GameSide(Side.CLIENT)
    public val currentGlId: Int
        //#if MC >= 1.21.5
        get() {
            upload()
            return (resources.glTexture as GlTexture?)?.glId ?: -1
        }
        //#else
        //$$ get() = getId()
        //#endif

    //#if MC <= 1.21.3
    //$$ override fun load(resourceManager: ResourceManager?) {
    //$$ }
    //#endif

    @GameSide(Side.CLIENT)
    public fun upload() {
        if (!isUploaded) {
            //#if MC >= 1.21.5
            val data = this.data ?: return
            val renderDevice = RenderSystem.getDevice()
            //#if MC >= 1.21.6
            val usage = GpuTexture.USAGE_TEXTURE_BINDING or GpuTexture.USAGE_COPY_SRC or GpuTexture.USAGE_COPY_DST
            val glTexture = renderDevice.createTexture(null as String?, usage, TextureFormat.RGBA8, width, height, 1, 1)
            //#else
            //$$ val glTexture = renderDevice.createTexture(null as String?, TextureFormat.RGBA8, width, height, 1)
            //#endif
            glTexture.setTextureFilter(FilterMode.NEAREST, true)
            renderDevice.createCommandEncoder().writeToTexture(glTexture, data.native)
            this.resources.glTexture = glTexture
            this.glTexture = glTexture
            //#if MC >= 1.21.6
            val view = renderDevice.createTextureView(glTexture)
            this.glTextureView = view
            resources.glTextureView = view
            //#endif
            //#else
            //$$ this.data?.prepareTexture(allocGlId())
            //$$ this.data?.uploadTexture(allocGlId())
            //$$ this.resources.glId = allocGlId()
            //#endif

            this.data = null
            this.isUploaded = true
            Resources.drainCleanupQueue()
        }
    }

    //#if MC >= 1.21.5
    //#if MC >= 1.21.6
    override fun getGlTextureView(): GpuTextureView {
        upload()
        return super.getGlTextureView()
    }

    override fun setUseMipmaps(mipmaps: Boolean) {
        upload()
        super.setUseMipmaps(mipmaps)
    }
    //#endif

    override fun setClamp(clamp: Boolean) {
        upload()
        super.setClamp(clamp)
    }

    override fun setFilter(bilinear: Boolean, mipmap: Boolean) {
        upload()
        super.setFilter(bilinear, mipmap)
    }

    override fun getGlTexture(): GpuTexture {
        upload()
        return super.getGlTexture()
    }

    override fun close() {
        super.close()
        resources.close()
    }
    //#elseif MC <= 1.21.4
    //$$ private fun allocGlId() = super.getId()
    //$$
    //$$ @GameSide(Side.CLIENT)
    //$$ override fun getId(): Int {
    //$$     upload()
    //$$     return super.getId()
    //$$ }
    //$$
    //$$ @GameSide(Side.CLIENT)
    //$$ override fun releaseId() {
    //$$     super.releaseId()
    //$$     resources.glId = -1
    //$$ }
    //$$
    //#if MC >= 1.16.5
    //$$ override fun close() {
    //$$     super.close()
    //$$     releaseId()
    //$$     resources.close()
    //$$ }
    //#endif
    //#endif

    private class Resources(
        referent: ReleasedDynamicTexture
    ) : PhantomReference<ReleasedDynamicTexture>(referent, referenceQueue), Closeable {

        //#if MC >= 1.21.5
        var glTexture: GpuTexture? = null
            set(value) {
                field?.close()
                field = value
            }

        //#if MC >= 1.21.6
        var glTextureView: GpuTextureView? = null
            set(value) {
                field?.close()
                field = value
            }
        //#endif
        //#else
        //$$ var glId: Int = -1
        //#endif

        var data: OmniImage? = null
            set(value) {
                field?.close()
                field = value
            }

        init {
            toBeCleanedUp.add(this)
        }

        override fun close() {
            toBeCleanedUp.remove(this)

            //#if MC >= 1.21.5
            glTexture = null
            //#if MC >= 1.21.6
            glTextureView = null
            //#endif
            //#else
            //$$ if (glId != -1) {
            //$$     OmniClient.textureManager.deleteTexture(glId)
            //$$     glId = -1
            //$$ }
            //#endif

            //#if MC <= 1.21.4
            //$$ data = null
            //#endif
        }

        companion object {

            val referenceQueue = ReferenceQueue<ReleasedDynamicTexture>()
            val toBeCleanedUp: MutableSet<Resources> = Collections.newSetFromMap(ConcurrentHashMap())

            fun drainCleanupQueue() {
                while (true) {
                    ((referenceQueue.poll() ?: break) as Resources).close()
                }
            }

        }

    }

}
