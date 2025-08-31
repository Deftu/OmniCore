package dev.deftu.omnicore.client.render.state

import dev.deftu.omnicore.api.annotations.VersionedBelow

//#if MC <= 1.16.5
//$$ import com.mojang.blaze3d.platform.GlStateManager
//#endif

@VersionedBelow("1.16.5")
public class OmniManagedAlphaState(
    public val isEnabled: Boolean,
    public val func: Int,
    public val ref: Float,
) {

    @VersionedBelow("1.16.5")
    public fun activate() {
        if (isEnabled) {
            enableAlpha()
            alphaFunc(func, ref)
        } else {
            disableAlpha()
        }
    }

    public companion object {

        @JvmField
        public val DISABLED: OmniManagedAlphaState = OmniManagedAlphaState(false, 0, 0f)

        @JvmStatic
        public fun active(): OmniManagedAlphaState {
            return OmniManagedAlphaState(
                isEnabled = false,
                func = 0,
                ref = 0f
            )
        }

        @JvmStatic
        public fun asEnabled(function: Int, reference: Float): OmniManagedAlphaState {
            return OmniManagedAlphaState(
                isEnabled = true,
                func = function,
                ref = reference
            )
        }

        @JvmStatic
        public fun enable(function: Int, reference: Float): OmniManagedAlphaState {
            return asEnabled(function, reference).also(OmniManagedAlphaState::activate)
        }

        @JvmStatic
        public fun disable(): OmniManagedAlphaState {
            return DISABLED.also(OmniManagedAlphaState::activate)
        }

        @JvmStatic
        public fun enableAlpha() {
            //#if MC <= 1.16.5
            //$$ GlStateManager.enableAlphaTest()
            //#endif
        }

        @JvmStatic
        public fun disableAlpha() {
            //#if MC <= 1.16.5
            //$$ GlStateManager.disableAlphaTest()
            //#endif
        }

        @JvmStatic
        public fun alphaFunc(func: Int, ref: Float) {
            //#if MC <= 1.16.5
            //$$ GlStateManager.alphaFunc(func, ref)
            //#endif
        }

    }

}
