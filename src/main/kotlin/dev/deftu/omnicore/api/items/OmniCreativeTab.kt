package dev.deftu.omnicore.api.items

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.text.Text
import net.minecraft.util.Identifier

public data class OmniCreativeTab @JvmOverloads public constructor(
    val id: Identifier,
    val icon: Item,
    val titleKey: String = "itemGroup.${id.namespace}.${id.path}",
) {
    private val entries = mutableListOf<Item>()

    public var isBuiltin: Boolean = false
        private set

    public val iconStack: ItemStack
        get() = ItemStack(icon)

    public val group: ItemGroup = FabricItemGroup.builder()
        .icon(::iconStack)
        .displayName(Text.translatable(titleKey))
        .build()

    public fun append(item: Item) {
        entries += item
    }

    public fun register(): OmniCreativeTab {
        val key = RegistryKey.of(RegistryKeys.ITEM_GROUP, id)
        Registry.register(Registries.ITEM_GROUP, key, group)
        ItemGroupEvents.modifyEntriesEvent(key).register { groupEntries ->
            for (item in entries) {
                groupEntries.add(item)
            }
        }

        return this
    }

    internal fun builtin(): OmniCreativeTab {
        isBuiltin = true
        return this
    }
}
