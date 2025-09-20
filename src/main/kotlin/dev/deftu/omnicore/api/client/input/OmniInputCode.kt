package dev.deftu.omnicore.api.client.input

import com.mojang.serialization.Codec

public sealed interface OmniInputCode {
    public companion object {
        public val CODEC: Codec<OmniInputCode?> = Codec.INT.xmap({ code ->
            OmniInputs.get(code)
        }, { it?.code })
    }

    public val code: Int
    public val isPressed: Boolean

    public val displayName: String
        get() = OmniInputs.getDisplayName(code)
}
