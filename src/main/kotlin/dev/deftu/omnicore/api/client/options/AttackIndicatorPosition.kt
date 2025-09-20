package dev.deftu.omnicore.api.client.options

//#if MC >= 1.16.5
import net.minecraft.client.option.AttackIndicator
//#endif

public enum class AttackIndicatorPosition {
    OFF,
    CROSSHAIR,
    HOTBAR;

    public companion object {
        public val isSupported: Boolean
            get() {
                //#if MC >= 1.9
                return true
                //#else
                //$$ return false
                //#endif
            }

        //#if MC >= 1.16.5
        public fun from(vanilla: AttackIndicator): AttackIndicatorPosition {
            return when (vanilla) {
                AttackIndicator.OFF -> OFF
                AttackIndicator.CROSSHAIR -> CROSSHAIR
                AttackIndicator.HOTBAR -> HOTBAR
            }
        }
        //#elseif MC >= 1.12.2
        //$$ public fun from(vanilla: Int): AttackIndicatorPosition {
        //$$     return when (vanilla) {
        //$$         0 -> OFF
        //$$         1 -> CROSSHAIR
        //$$         2 -> HOTBAR
        //$$         else -> throw IllegalArgumentException("Unknown attack indicator value: $vanilla")
        //$$     }
        //$$ }
        //#endif
    }
}
