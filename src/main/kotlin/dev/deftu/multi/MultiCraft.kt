package dev.deftu.multi

public object MultiCraft {
    public val debug: Boolean
        get() = System.getProperty("multicraft.debug")?.toBoolean() ?: false
}
