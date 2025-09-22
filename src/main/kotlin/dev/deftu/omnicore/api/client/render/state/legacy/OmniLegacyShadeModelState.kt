package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.state.TrackedState

//#if MC <= 1.16.5
//$$ import org.lwjgl.opengl.GL11
//#endif

public data class OmniLegacyShadeModelState(public val mode: ShadeModel) : TrackedState<OmniLegacyShadeModelState> {
    public companion object {
        @JvmField public val FLAT: OmniLegacyShadeModelState = OmniLegacyShadeModelState(ShadeModel.FLAT)
        @JvmField public val SMOOTH: OmniLegacyShadeModelState = OmniLegacyShadeModelState(ShadeModel.SMOOTH)
    }

    override var prevState: OmniLegacyShadeModelState? = null
        private set

    override fun submit(saveLast: Boolean) {
        //#if MC <= 1.16.5
        //$$ if (saveLast) {
        //$$     prevState = OmniLegacyRenderStates.shadeModel
        //$$ }
        //$$
        //$$ GL11.glShadeModel(mode.const)
        //#endif
    }
}
