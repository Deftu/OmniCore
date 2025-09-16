package dev.deftu.omnicore.api.items

import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Experimental
@Deprecated(message = "Unstable API. Do not use until further notice.", level = DeprecationLevel.WARNING)
public interface OmniCreativeTab {
    public companion object {
        public const val DEFAULT_TITLE_KEY: String = "itemGroup.%s.%s"
    }

    public val isBuiltin: Boolean

    public val id: Identifier
    public val icon: () -> Item
    public val titleKey: String

    public val vanilla: ItemGroup?

    public fun append(vararg items: Item): OmniCreativeTab
    public fun register(): OmniCreativeTab
}
