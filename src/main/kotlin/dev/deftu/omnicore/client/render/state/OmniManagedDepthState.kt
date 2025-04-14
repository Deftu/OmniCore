package dev.deftu.omnicore.client.render.state

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import org.lwjgl.opengl.GL11

//#if MC >= 1.21.5
//$$ import com.mojang.blaze3d.opengl.GlStateManager
//#elseif MC >= 1.17.1
import com.mojang.blaze3d.systems.RenderSystem
//#else
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

@GameSide(Side.CLIENT)
public data class OmniManagedDepthState(
    public val isEnabled: Boolean,
    public val function: DepthFunction,
    public val isMask: Boolean
) {

    public fun activate() {
        if (isEnabled) {
            enableDepth()
            depthFunc(function.value)
            depthMask(isMask)
        } else {
            disableDepth()
        }
    }

    public companion object {

        @JvmField
        public val DISABLED: OmniManagedDepthState = OmniManagedDepthState(false, DepthFunction.LESS, false)

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun active(): OmniManagedDepthState {
            return OmniManagedDepthState(
                isEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST),
                function = DepthFunction.active(),
                isMask = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK)
            )
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun asEnabled(function: DepthFunction, isMask: Boolean): OmniManagedDepthState {
            return OmniManagedDepthState(
                isEnabled = true,
                function = function,
                isMask = isMask
            )
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun enable(function: DepthFunction, isMask: Boolean): OmniManagedDepthState {
            return asEnabled(function, isMask).also(OmniManagedDepthState::activate)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun disable(): OmniManagedDepthState {
            return DISABLED.also(OmniManagedDepthState::activate)
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun enableDepth() {
            //#if MC >= 1.21.5
            //$$ GlStateManager._enableDepthTest()
            //#elseif MC >= 1.17.1
            RenderSystem.enableDepthTest()
            //#elseif MC >= 1.16.5
            //$$ GlStateManager.enableDepthTest()
            //#else
            //$$ GlStateManager.enableDepth()
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun disableDepth() {
            //#if MC >= 1.21.5
            //$$ GlStateManager._disableDepthTest()
            //#elseif MC >= 1.17.1
            RenderSystem.disableDepthTest()
            //#elseif MC >= 1.16.5
            //$$ GlStateManager.disableDepthTest()
            //#else
            //$$ GlStateManager.disableDepth()
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun depthFunc(func: Int) {
            //#if MC >= 1.21.5
            //$$ GlStateManager._depthFunc(func)
            //#elseif MC >= 1.17.1
            RenderSystem.depthFunc(func)
            //#else
            //$$ GlStateManager.depthFunc(func)
            //#endif
        }

        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun depthMask(mask: Boolean) {
            //#if MC >= 1.21.5
            //$$ GlStateManager._depthMask(mask)
            //#elseif MC >= 1.17.1
            RenderSystem.depthMask(mask)
            //#else
            //$$ GlStateManager.depthMask(mask)
            //#endif
        }

    }

}
