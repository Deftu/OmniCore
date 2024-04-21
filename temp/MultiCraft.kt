package dev.deftu.omnicore

public object MultiCraft {
    public val debug: Boolean
        get() = System.getProperty("multicraft.debug")?.toBoolean() ?: false
}
