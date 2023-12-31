@file:Suppress("MemberVisibilityCanBePrivate")

package dev.deftu.multi

//#if MC >= 1.15
import net.minecraft.client.texture.NativeImage
//#if FORGE
//$$ import net.minecraft.client.renderer.texture.*
//#else
//#endif
//#else
//$$ import net.minecraft.client.renderer.texture.*
//#endif

import com.mojang.blaze3d.platform.GlStateManager
import com.mojang.blaze3d.platform.TextureUtil
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.NativeImageBackedTexture
import net.minecraft.client.texture.TextureManager
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.InputStream
import java.lang.ref.PhantomReference
import java.lang.ref.ReferenceQueue
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.imageio.ImageIO

public class MultiTextureManager(
    private val textureManager: TextureManager
) {
    public companion object {
        @JvmStatic
        public val INSTANCE: MultiTextureManager by lazy {
            MultiTextureManager(get())
        }

        @JvmStatic
        public fun get(): TextureManager = MultiClient.getInstance().textureManager

        @JvmStatic
        public fun getActiveTexture(): Int =
            GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)

        @JvmStatic
        public fun setActiveTexture(id: Int) {
            //#if MC >= 1.17
            GlStateManager._activeTexture(id)
            //#elseif MC >= 1.14
            //$$ GlStateManager.activeTexture(id)
            //#else
            //$$ GlStateManager.setActiveTexture(id)
            //#endif
        }

        @JvmStatic
        public fun generateTexture(): Int {
            //#if MC >= 1.17
            return TextureUtil.generateTextureId()
            //#elseif MC >= 1.16.5
            //$$ return TextureUtil.generateId()
            //#else
            //$$ // TODO
            //$$ return 0
            //#endif
        }

        @JvmStatic
        public fun bindTexture(id: Int) {
            //#if MC >= 1.17
            GlStateManager._bindTexture(id)
            //#else
            //$$ GlStateManager.bindTexture(id)
            //#endif
        }

        @JvmStatic
        public fun removeTexture() {
            bindTexture(GL11.GL_NONE)
        }

        @JvmStatic
        public fun deleteTexture(id: Int) {
            //#if MC >= 1.17
            GlStateManager._deleteTexture(id)
            //#else
            //$$ GlStateManager.deleteTexture(id)
            //#endif
        }

        @JvmStatic
        public fun configureTexture(id: Int, block: Runnable) {
            val prevActiveTexture = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
            bindTexture(id)
            block.run()
            bindTexture(prevActiveTexture)
        }

        @JvmStatic
        public fun configureTextureUnit(index: Int, block: Runnable) {
            val prevActiveTexture = getActiveTexture()
            setActiveTexture(GL13.GL_TEXTURE0 + index)
            block.run()
            setActiveTexture(prevActiveTexture)
        }
    }

    public fun getReleasedDynamicTexture(stream: InputStream): ReleasedDynamicTexture {
        try {
            //#if MC >= 1.14
            val image = NativeImage.read(stream)
            //#else
            //$$ val image = ImageIO.read(stream)
            //#endif
            return ReleasedDynamicTexture(image)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    public fun bindTexture(path: Identifier): MultiTextureManager = apply {
        textureManager.bindTexture(path)
    }

    //#if MC >= 1.14
    public fun registerTexture(path: Identifier, texture: AbstractTexture): MultiTextureManager = apply {
    //#else
    //$$ public fun registerTexture(path: ResourceLocation, texture: ITextureObject): MultiTextureManager = apply {
    //#endif
        textureManager.registerTexture(path, texture)
    }

    public fun registerImageTexture(path: Identifier, texture: BufferedImage): MultiTextureManager = apply {
        val stream = ByteArrayOutputStream()
        ImageIO.write(texture, "png", stream)
        MultiClient.execute {
            registerTexture(path, getReleasedDynamicTexture(stream.toByteArray().inputStream()))
        }
    }

    public fun registerDynamicTexture(path: String, texture: NativeImageBackedTexture): MultiTextureManager = apply {
        textureManager.registerDynamicTexture(path, texture)
    }

    public fun destroyTexture(path: Identifier): MultiTextureManager = apply {
        textureManager.destroyTexture(path)
    }

    public fun deleteTexture(id: Int): MultiTextureManager = apply {
        MultiTextureManager.deleteTexture(id)
    }
}

/**
 * Adapted from EssentialGG UniversalCraft under LGPL-3.0
 * https://github.com/EssentialGG/UniversalCraft/blob/f4917e139b5f6e5346c3bafb6f56ce8877854bf1/LICENSE
 */
public class ReleasedDynamicTexture(
    public val width: Int,
    public val height: Int,
    //#if MC >= 1.14
    data: NativeImage?
    //#else
    //$$ data: IntArray?
    //#endif
//#if MC >= 1.14
) : AbstractTexture() {
//#else
//$$ ) : AbstractTexture() {
//#endif
    public constructor(width: Int, height: Int) : this(width, height, null)
    //#if MC >= 1.14
    public constructor(image: NativeImage) : this(image.width, image.height, image)
    //#else
    //$$ public constructor(image: BufferedImage) : this(image.width, image.height, image.getRGB(0, 0, image.width, image.height, null, 0, image.width))
    //#endif

    private var resources = Resources(this)
    //#if MC >= 1.14
    private var data by resources::data

    init {
        resources.data = data ?: NativeImage(width, height, true)
    }
    //#else
    //$$ private var data = data ?: IntArray(width * height)
    //#endif
    public var uploaded: Boolean = false

    override fun load(resourceManager: ResourceManager?) {
    }

    private fun allocGlId() = super.getGlId()

    public fun upload() {
        if (!uploaded) {
            //#if MC >= 1.17
            TextureUtil.prepareImage(allocGlId(), width, height)
            //#elseif MC >= 1.16
            //$$ TextureUtil.allocate(allocGlId(), width, height)
            //#elseif MC >= 1.14
            //$$ TextureUtil.prepareImage(allocGlId(), width, height)
            //#else
            //$$ TextureUtil.allocateTexture(allocGlId(), width, height)
            //#endif

            //#if MC >= 1.14
            MultiTextureManager.configureTexture(allocGlId()) {
                data?.upload(0, 0, 0, false)
            }
            data = null
            //#else
            //$$ TextureUtil.uploadTexture(allocGlId(), data, width, height)
            //$$ data = IntArray(0)
            //#endif

            uploaded = true
            resources.glId = allocGlId()
            Resources.drainCleanupQueue()
        }
    }

    override fun getGlId(): Int {
        upload()
        return super.getGlId()
    }

    override fun clearGlId() {
        super.clearGlId()
        resources.glId = -1
    }

    private class Resources(
        referent: ReleasedDynamicTexture
    ) : PhantomReference<ReleasedDynamicTexture>(referent, referenceQueue), Closeable {
        var glId: Int = -1
        //#if MC >= 1.14
        var data: NativeImage? = null
            set(value) {
                field?.close()
                field = value
            }
        //#endif

        init {
            toBeCleanedUp.add(this)
        }

        override fun close() {
            toBeCleanedUp.remove(this)
            if (glId != -1) {
                MultiClient.getTextureManager().deleteTexture(glId)
                glId = -1
            }

            //#if MC >= 1.14
            data = null
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
