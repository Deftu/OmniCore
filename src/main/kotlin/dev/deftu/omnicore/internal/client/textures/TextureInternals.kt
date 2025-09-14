package dev.deftu.omnicore.internal.client.textures

import com.mojang.blaze3d.opengl.GlStateManager
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

//#if MC >= 1.16.5
import com.mojang.blaze3d.systems.RenderSystem
//#endif

//#if MC >= 1.16.5 && MC <= 1.21.4
//$$ import com.mojang.blaze3d.platform.TextureUtil
//#endif

@ApiStatus.Internal
public object TextureInternals {
    @JvmStatic
    public var active: Int
        get() = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
        set(value) {
            bind0(value)
        }

    @JvmStatic
    public var activeUnit: Int
        get() = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)
        set(value) {
            //#if MC >= 1.17
            GlStateManager._activeTexture(value)
            //#elseif MC >= 1.14
            //$$ GlStateManager.activeTexture(value)
            //#else
            //$$ GlStateManager.setActiveTexture(value)
            //#endif
        }

    @JvmStatic
    public fun create(): Int {
        //#if MC >= 1.21.5 || MC <= 1.12.2
        return GL11.glGenTextures()
        //#elseif MC >= 1.17
        //$$ return TextureUtil.generateTextureId()
        //#elseif MC >= 1.16.5
        //$$ return TextureUtil.generateId()
        //#endif
    }

    @JvmStatic
    public fun bound(): Int {
        return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D)
    }

    @JvmStatic
    public fun boundOnUnit(unit: Int): Int {
        val prevActiveTexture = activeUnit
        activeUnit = GL13.GL_TEXTURE0 + unit
        val bound = bound()
        activeUnit = prevActiveTexture
        return bound
    }

    @JvmStatic
    public fun bind0(id: Int) {
        //#if MC >= 1.17.1
        GlStateManager._bindTexture(id)
        //#else
        //$$ GlStateManager.bindTexture(id)
        //#endif
    }

    @JvmStatic
    public fun bindOnUnit0(unit: Int, id: Int) {
        //#if MC >= 1.21.6
        RenderSystem.setShaderTexture(unit, RenderSystem.getDevice().createTextureView(WrappedGlTexture(id)))
        //#elseif MC >= 1.21.5
        //$$ RenderSystem.setShaderTexture(unit, WrappedGlTexture(id))
        //#elseif MC >= 1.17.1
        //$$ RenderSystem.setShaderTexture(unit, id)
        //#else
        //$$ activeUnit = GL13.GL_TEXTURE0 + unit
        //$$ active = id
        //#endif
    }

    @JvmStatic
    public fun bind(id: Int): () -> Unit {
        val prevBoundTexture = bound()
        bind0(id)
        return {
            bind0(prevBoundTexture)
        }
    }

    @JvmStatic
    public fun bindOnUnit(unit: Int, id: Int): () -> Unit {
        val prevBoundTexture = boundOnUnit(unit)
        bindOnUnit0(unit, id)
        return {
            bindOnUnit0(unit, prevBoundTexture)
        }
    }

    @JvmStatic
    public fun unbind() {
        bind(GL11.GL_NONE)
    }

    @JvmStatic
    public fun unbindOnUnit(unit: Int) {
        bindOnUnit(unit, GL11.GL_NONE)
    }

    @JvmStatic
    public fun delete(id: Int) {
        //#if MC >= 1.17
        GlStateManager._deleteTexture(id)
        //#else
        //$$ GlStateManager.deleteTexture(id)
        //#endif
    }
}
