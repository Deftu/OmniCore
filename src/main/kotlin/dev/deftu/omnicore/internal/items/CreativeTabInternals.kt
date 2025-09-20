package dev.deftu.omnicore.internal.items

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

//#if FABRIC && MC >= 1.16.5
//#if MC >= 1.19.4
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
//#else
//$$ import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
//#endif
//#endif

//#if FORGE-LIKE && MC >= 1.19.4
//#if MC >= 1.20.1
//#if FORGE
//$$ import net.minecraftforge.registries.DeferredRegister
//#else
//$$ import net.neoforged.neoforge.registries.DeferredRegister
//#endif
//#else
//$$ import net.minecraftforge.event.CreativeModeTabEvent
//#endif
//$$
//$$ import dev.deftu.omnicore.internal.modEventBus
//$$ import net.minecraft.core.registries.Registries
//$$ import java.util.function.Supplier
//#endif

@ApiStatus.Internal
public object CreativeTabInternals {
    //#if FORGE-LIKE
    //#if MC >= 1.20.1
    //$$ private val registries = hashMapOf<String, DeferredRegister<CreativeModeTab>>()
    //#else
    //$$ private data class TabEntry(
    //$$     val resultOutput: CompletableFuture<CreativeModeTab>,
    //$$     val icon: ItemStack,
    //$$     val titleKey: String,
    //$$     val entries: List<Item>
    //$$ )
    //$$
    //$$ private val entries = hashMapOf<ResourceLocation, TabEntry>()
    //#endif
    //#endif

    @JvmStatic
    public fun initialize() {
        //#if FORGE && MC == 1.19.4
        //$$ modEventBus.addListener<CreativeModeTabEvent.Register> { event ->
        //$$     for ((location, tab) in entries) {
        //$$         event.registerCreativeModeTab(location) { builder ->
        //$$             builder
        //$$                 .icon { tab.icon }
        //$$                 .title(Component.translatable(tab.titleKey))
        //$$                 .displayItems { _, output -> tab.entries.forEach(output::accept) }
        //$$                 .build().also { tab.resultOutput.complete(it) }
        //$$         }
        //$$     }
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun registerGroup(
        id: Identifier,
        icon: ItemStack,
        titleKey: String,
        entries: List<Item>,
    ): CompletableFuture<ItemGroup> {
        //#if FABRIC && MC >= 1.16.5
        //#if MC >= 1.20.1
        val key = RegistryKey.of(RegistryKeys.ITEM_GROUP, id)
        val group = FabricItemGroup.builder()
            .icon { icon }
            .displayName(Text.translatable(titleKey))
            .build()
        ItemGroupEvents.modifyEntriesEvent(key).register { output ->
            for (item in entries) {
                output.add(item)
            }
        }

        return CompletableFuture.completedFuture(group)
        //#elseif MC >= 1.19.4
        //$$ val group = FabricItemGroup.builder(id)
        //$$     .icon { icon }
        //$$     .displayName(Text.translatable(titleKey))
        //$$     .build()
        //$$ ItemGroupEvents.modifyEntriesEvent(group).register { output ->
        //$$     for (item in entries) {
        //$$         output.add(item)
        //$$     }
        //$$ }
        //$$
        //$$ return CompletableFuture.completedFuture(group)
        //#else
        //$$ return FabricItemGroupBuilder.create(id)
        //$$     .icon { icon }
        //$$     .appendItems { list -> list.addAll(entries.map(::ItemStack)) }
        //$$     .build()
        //$$     .let(CompletableFuture<ItemGroup>::completedFuture)
        //#endif
        //#else
        //#if MC >= 1.20.1
        //$$ return registryFor(id.namespace).register(id.path, Supplier {
        //$$     CreativeModeTab.builder()
        //$$         .icon { icon }
        //$$         .title(Component.translatable(titleKey))
        //$$         .displayItems { _, output -> entries.forEach(output::accept) }
        //$$         .build()
        //$$ }).get().let(CompletableFuture<CreativeModeTab>::completedFuture)
        //#elseif MC >= 1.19.4
        //$$ val result = CompletableFuture<CreativeModeTab>()
        //$$ this.entries[id] = TabEntry(result, icon, titleKey, entries)
        //$$ return result
        //#else
        //$$ return object : CreativeModeTab(
                //#if FABRIC && MC < 1.16.5
                //$$ itemGroups.size,
                //#endif
        //$$     id.toString()
        //$$ ) {
                //#if MC >= 1.12.2
                //$$ override fun makeIcon(): ItemStack = icon
                //#else
                //$$ override fun getIconItem(): Item = icon.item
                //#endif
        //$$ }.let(CompletableFuture<CreativeModeTab>::completedFuture)
        //#endif
        //#endif
    }

    //#if MC <= 1.12.2
    //$$ @JvmStatic
    //$$ public fun assignToItem(group: CreativeTabs?, items: Array<out Item>) {
    //$$     if (group == null) {
    //$$         return
    //$$     }
    //$$
    //$$     for (item in items) {
    //$$         item.creativeTab = group
    //$$     }
    //$$ }
    //#endif

    //#if FORGE-LIKE && MC >= 1.20.1
    //$$ private fun registryFor(namespace: String): DeferredRegister<CreativeModeTab> {
    //$$     return registries.getOrPut(namespace) {
    //$$         DeferredRegister.create(Registries.CREATIVE_MODE_TAB, namespace).also {
    //$$             it.register(modEventBus)
    //$$         }
    //$$     }
    //$$ }
    //#endif
}
