package dev.deftu.omnicore.client.options

//#if MC >= 1.9
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
                // return false
                //#endif
            }

        //#if MC >= 1.9
        public fun from(vanilla: AttackIndicator): AttackIndicatorPosition {
            return when (vanilla) {
                AttackIndicator.OFF -> OFF
                AttackIndicator.CROSSHAIR -> CROSSHAIR
                AttackIndicator.HOTBAR -> HOTBAR
            }
        }
        //#endif

    }

}
