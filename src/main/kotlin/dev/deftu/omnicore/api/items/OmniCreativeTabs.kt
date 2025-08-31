package dev.deftu.omnicore.api.items

import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemGroups
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey

public object OmniCreativeTabs {
    @JvmStatic
    public val buildingBlocks: OmniCreativeTab
        get() = wrap(ItemGroups.BUILDING_BLOCKS).builtin()

    @JvmStatic
    public fun wrap(group: ItemGroup): OmniCreativeTab {
        val id = Registries.ITEM_GROUP.getId(group) ?: error("ItemGroup is not registered in the registry")
        return OmniCreativeTab(id, group.icon.item)
    }

    @JvmStatic
    public fun wrap(key: RegistryKey<ItemGroup>): OmniCreativeTab {
        val group = Registries.ITEM_GROUP.get(key) ?: error("ItemGroup $key is not registered in the registry")
        return OmniCreativeTab(key.value, group.icon.item)
    }
}
