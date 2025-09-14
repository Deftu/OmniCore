package dev.deftu.omnicore.api.equipment

//#if MC >= 1.12.2
import net.minecraft.entity.EquipmentSlot
//#endif

public sealed interface EquipmentType {
    public val isHand: Boolean
        get() = this is MainHand || this is OffHand

    public val isArmor: Boolean
        get() = this is Armor

    public data object MainHand : EquipmentType
    public data object OffHand : EquipmentType
    public data class Armor(
        //#if MC >= 1.12.2
        val slot: EquipmentSlot
        //#else
        //$$ val slot: Int
        //#endif
    ) : EquipmentType {
        public companion object {
            //#if MC >= 1.12.2
            @JvmField
            public val HEAD: Armor = Armor(EquipmentSlot.HEAD)

            @JvmField
            public val CHEST: Armor = Armor(EquipmentSlot.CHEST)

            @JvmField
            public val LEGS: Armor = Armor(EquipmentSlot.LEGS)

            @JvmField
            public val FEET: Armor = Armor(EquipmentSlot.FEET)
            //#else
            //$$ @JvmField
            //$$ public val HEAD: Armor = Armor(3)
            //$$
            //$$ @JvmField
            //$$ public val CHEST: Armor = Armor(2)
            //$$
            //$$ @JvmField
            //$$ public val LEGS: Armor = Armor(1)
            //$$
            //$$ @JvmField
            //$$ public val FEET: Armor = Armor(0)
            //#endif
        }
    }
}
