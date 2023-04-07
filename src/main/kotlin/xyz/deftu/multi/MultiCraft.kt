package xyz.deftu.multi

object MultiCraft {
    val debug: Boolean
        get() = System.getProperty("multicraft.debug")?.toBoolean() ?: false
}
