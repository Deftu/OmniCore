package dev.deftu.omnicore.internal.items

import dev.deftu.omnicore.api.items.OmniCreativeTab
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

@ApiStatus.Internal
public data class OmniCreativeTabImpl(
    override val id: Identifier,
    override val icon: () -> Item,
    override val titleKey: String,
) : OmniCreativeTab {
    private val entries = mutableListOf<Item>()
    private var isRegistered = false
    private lateinit var resultingFuture: CompletableFuture<ItemGroup>

    override val vanilla: ItemGroup?
        get() = if (isRegistered) resultingFuture.get() else null

    override var isBuiltin: Boolean = false
        private set

    override fun append(vararg items: Item): OmniCreativeTab {
        entries += items
        //#if MC <= 1.12.2
        //$$ CreativeTabInternals.assignToItem(vanilla, items)
        //#endif
        return this
    }

    override fun register(): OmniCreativeTab {
        check(!isRegistered) { "Item group $id already registered" }
        resultingFuture = CreativeTabInternals.registerGroup(id, ItemStack(icon()), titleKey, entries)
        isRegistered = true
        return this
    }

    public fun builtin(): OmniCreativeTab {
        isBuiltin = true
        return this
    }
}
