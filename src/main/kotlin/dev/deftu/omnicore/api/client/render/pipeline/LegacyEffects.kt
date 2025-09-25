package dev.deftu.omnicore.api.client.render.pipeline

import dev.deftu.omnicore.api.client.render.OmniTextureUnit
import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyAlphaState
import dev.deftu.omnicore.api.client.render.state.legacy.OmniLegacyLineStippleState
import dev.deftu.omnicore.api.client.render.state.legacy.ShadeModel

public data class LegacyEffects(
    @JvmField public var alpha: OmniLegacyAlphaState = OmniLegacyAlphaState.DISABLED,
    @JvmField public var lighting: Boolean = false,
    @JvmField public var lineStipple: Boolean = false,
    @JvmField public var shadeModel: ShadeModel = ShadeModel.SMOOTH,
    @JvmField public var textureStates: Map<OmniTextureUnit, Boolean> = mapOf()
) {
    public class Builder {
        @JvmField public var alpha: OmniLegacyAlphaState = OmniLegacyAlphaState.DISABLED
        @JvmField public var lighting: Boolean = false
        @JvmField public var lineStipple: Boolean = false
        @JvmField public var shadeModel: ShadeModel = ShadeModel.SMOOTH
        private val textureStates: MutableMap<OmniTextureUnit, Boolean> = mutableMapOf()

        public constructor(options: LegacyEffects? = null) {
            if (options != null) {
                this.alpha = options.alpha
                this.lighting = options.lighting
                this.lineStipple = options.lineStipple
                this.shadeModel = options.shadeModel
                this.textureStates.putAll(options.textureStates)
            }
        }

        public fun setAlpha(alpha: OmniLegacyAlphaState): Builder {
            this.alpha = alpha
            return this
        }

        public fun setLighting(lighting: Boolean): Builder {
            this.lighting = lighting
            return this
        }

        public fun setLineStipple(lineStipple: Boolean): Builder {
            this.lineStipple = lineStipple
            return this
        }

        public fun setShadeModel(shadeModel: ShadeModel): Builder {
            this.shadeModel = shadeModel
            return this
        }

        public fun setTexture(unit: OmniTextureUnit, state: Boolean): Builder {
            this.textureStates[unit] = state
            return this
        }

        public infix fun OmniTextureUnit.equals(state: Boolean): Builder {
            return setTexture(this, state)
        }

        public fun build(): LegacyEffects {
            return LegacyEffects(alpha, lighting, lineStipple, shadeModel, textureStates.toMap())
        }
    }
}
