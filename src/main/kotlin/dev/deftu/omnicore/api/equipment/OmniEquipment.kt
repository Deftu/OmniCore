@file:JvmName("OmniEquipment")

package dev.deftu.omnicore.api.equipment

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack

//#if MC < 1.12.2
//$$ import net.minecraft.entity.player.EntityPlayer
//#endif

public fun LivingEntity.stack(target: EquipmentType): ItemStack? {
    //#if MC < 1.12.2
    //$$ require(this is EntityPlayer) { "Only players have equipment slots in MC versions before 1.12.2" }
    //#endif

    return when (target) {
        //#if MC >= 1.12.2
        is EquipmentType.MainHand -> this.mainHandItem
        is EquipmentType.OffHand -> this.offhandItem
        is EquipmentType.Armor -> this.getItemBySlot(target.slot)
        //#else
        //$$ is EquipmentType.MainHand -> this.heldItem
        //$$ is EquipmentType.OffHand -> this.heldItem
        //$$ is EquipmentType.Armor -> this.getEquipmentInSlot(target.slot)
        //#endif
    }
}

public operator fun LivingEntity.get(target: EquipmentType): ItemStack? {
    return this.stack(target)
}
