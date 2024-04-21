package dev.deftu.omnicore

public object OmniCore {
    public val debug: Boolean
        get() = System.getProperty("omnicore.debug")?.toBoolean() ?: false
}
