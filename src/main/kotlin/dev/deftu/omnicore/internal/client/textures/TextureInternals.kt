package dev.deftu.omnicore.internal.client.textures

import com.mojang.blaze3d.opengl.GlStateManager
import dev.deftu.omnicore.api.client.textureManager
import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import net.minecraft.resources.ResourceLocation
import org.jetbrains.annotations.ApiStatus
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL13

//#if MC >= 1.21.5
import com.mojang.blaze3d.opengl.GlTexture
//#endif

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
            GlStateManager._bindTexture(value)
        }

    @JvmStatic
    public var activeUnit: OmniTextureUnit
        get() = OmniTextureUnit.gl(GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE)) ?: OmniTextureUnit.TEXTURE0
        set(value) {
            //#if MC >= 1.16.5
            GlStateManager._activeTexture(value.const)
            //#else
            //$$ GlStateManager.setActiveTexture(value.const)
            //#endif
        }

    @JvmStatic
    public val states: Map<OmniTextureUnit, Boolean>
        get() {
            return buildMap {
                for (unit in OmniTextureUnit.ALL) {
                    put(unit, isEnabled(unit))
                }
            }
        }

    @JvmStatic
    public fun create(): Int {
        //#if MC >= 1.21.5 || MC <= 1.12.2
        return GL11.glGenTextures()
        //#elseif MC >= 1.16.5
        //$$ return TextureUtil.generateTextureId()
        //#endif
    }

    @JvmStatic
    public fun boundOnUnit(unit: OmniTextureUnit): Int {
        val prevActiveTexture = activeUnit
        activeUnit = unit
        val bound = active
        activeUnit = prevActiveTexture
        return bound
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
        //$$ activeUnit = OmniTextureUnit.from(unit) ?: throw IllegalArgumentException("Invalid texture unit: $unit")
        //$$ active = id
        //#endif
    }

    @JvmStatic
    public fun bind(id: Int): () -> Unit {
        val prevBoundTexture = active
        active = id
        return {
            active = prevBoundTexture
        }
    }

    @JvmStatic
    public fun bindOnUnit(unit: OmniTextureUnit, id: Int): () -> Unit {
        val prevBoundTexture = boundOnUnit(unit)
        bindOnUnit0(unit.id, id)
        return {
            bindOnUnit0(unit.id, prevBoundTexture)
        }
    }

    @JvmStatic
    public fun unbind() {
        bind(GL11.GL_NONE)
    }

    @JvmStatic
    public fun unbindOnUnit(unit: OmniTextureUnit) {
        bindOnUnit(unit, GL11.GL_NONE)
    }

    @JvmStatic
    public fun delete(id: Int) {
        GlStateManager._deleteTexture(id)
    }

    @JvmStatic
    public fun obtainId(location: ResourceLocation): Int {
        val texture = textureManager.getTexture(location)
        //#if MC <= 1.16.5
        //$$ if (texture == null) {
        //$$     throw IllegalArgumentException("Texture not found: $location")
        //$$ }
        //#endif

        //#if MC >= 1.21.5
        val gpuTexture = texture.texture
        return (gpuTexture as GlTexture).glId()
        //#else
        //$$ return texture.id
        //#endif
    }

    @JvmStatic
    public fun isEnabled(unit: OmniTextureUnit): Boolean {
        //#if MC >= 1.18.2
        return true
        //#elseif MC >= 1.17.1
        //$$ return RenderSystem.getTextureId(unit.id) != 0
        //#else
        //$$ val prevActiveUnit = activeUnit
        //$$ activeUnit = unit
        //$$ val enabled = GL11.glIsEnabled(GL11.GL_TEXTURE_2D)
        //$$ activeUnit = prevActiveUnit
        //$$ return enabled
        //#endif
    }

    @JvmStatic
    public fun enable(unit: OmniTextureUnit) {
        //#if MC <= 1.19.2
        //$$ val prevActiveUnit = activeUnit
        //$$ activeUnit = unit
        //#if MC >= 1.17.1
        //$$ RenderSystem.enableTexture()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager._enableTexture()
        //#else
        //$$ GlStateManager.enableTexture2D()
        //#endif
        //$$ activeUnit = prevActiveUnit
        //#endif
    }

    @JvmStatic
    public fun disable(unit: OmniTextureUnit) {
        //#if MC <= 1.19.2
        //$$ val prevActiveUnit = activeUnit
        //$$ activeUnit = unit
        //#if MC >= 1.17.1
        //$$ RenderSystem.disableTexture()
        //#elseif MC >= 1.16.5
        //$$ GlStateManager._disableTexture()
        //#else
        //$$ GlStateManager.disableTexture2D()
        //#endif
        //$$ activeUnit = prevActiveUnit
        //#endif
    }
}
