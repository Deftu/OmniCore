package dev.deftu.omnicore.api.client.render.state.legacy

import dev.deftu.omnicore.api.client.render.state.TrackedState

//#if MC <= 1.16.5
//$$ import dev.deftu.omnicore.internal.client.render.state.legacy.AlphaStateInternals
//#endif

public data class OmniLegacyAlphaState(
    public val isEnabled: Boolean,
    public val func: Int,
    public val ref: Float,
) : TrackedState<OmniLegacyAlphaState> {
    override var prevState: OmniLegacyAlphaState? = null
        private set

    override fun submit(saveLast: Boolean) {
        //#if MC <= 1.16.5
        //$$ if (saveLast) {
        //$$     prevState = OmniLegacyRenderStates.alpha
        //$$ }
        //$$
        //$$ if (isEnabled) {
        //$$     AlphaStateInternals.enable()
        //$$     AlphaStateInternals.configure(func, ref)
        //$$ } else {
        //$$     AlphaStateInternals.disable()
        //$$ }
        //#endif
    }

    public fun asEnabled(): OmniLegacyAlphaState {
        return if (isEnabled) this else copy(isEnabled = true)
    }

    public fun asDisabled(): OmniLegacyAlphaState {
        return if (!isEnabled) this else copy(isEnabled = false)
    }

    public fun withFunction(function: Int): OmniLegacyAlphaState {
        return if (this.func == function) this else copy(func = function)
    }

    public fun withRef(ref: Float): OmniLegacyAlphaState {
        return if (this.ref == ref) this else copy(ref = ref)
    }

    public companion object {
        @JvmField public val DISABLED: OmniLegacyAlphaState = OmniLegacyAlphaState(false, 0, 0f)

        @JvmStatic
        public fun asEnabled(function: Int, reference: Float): OmniLegacyAlphaState {
            return OmniLegacyAlphaState(
                isEnabled = true,
                func = function,
                ref = reference
            )
        }
    }
}