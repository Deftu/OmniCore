package dev.deftu.omnicore.api.client.input

public sealed interface OmniInputCode {
    public val code: Int
    public val isPressed: Boolean

    public val displayName: String
        get() = OmniInputs.getDisplayName(code)
}
