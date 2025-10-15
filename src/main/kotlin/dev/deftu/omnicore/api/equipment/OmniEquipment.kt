@file:JvmName("OmniEquipment")

package dev.deftu.omnicore.api.equipment

import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack

//#if MC < 1.12.2
//$$ import net.minecraft.entity.player.PlayerEntity
//#endif

public fun LivingEntity.stack(target: EquipmentType): ItemStack? {
    //#if MC < 1.12.2
    //$$ require(this is PlayerEntity) { "Only players have equipment slots in MC versions before 1.12.2" }
    //#endif

    return when (target) {
        //#if MC >= 1.12.2
        is EquipmentType.MainHand -> this.mainHandStack
        is EquipmentType.OffHand -> this.offHandStack
        is EquipmentType.Armor -> this.getEquippedStack(target.slot)
        //#else
        //$$ is EquipmentType.MainHand -> this.mainHandStack
        //$$ is EquipmentType.OffHand -> this.mainHandStack
        //$$ is EquipmentType.Armor -> this.getArmorSlot(target.slot)
        //#endif
    }
}

public operator fun LivingEntity.get(target: EquipmentType): ItemStack? {
    return this.stack(target)
}
