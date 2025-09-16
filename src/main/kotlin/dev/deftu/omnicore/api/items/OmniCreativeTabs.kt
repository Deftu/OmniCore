package dev.deftu.omnicore.api.items

import dev.deftu.omnicore.internal.items.OmniCreativeTabImpl
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import org.jetbrains.annotations.ApiStatus

@Deprecated(message = "Unstable API. Do not use until further notice.", level = DeprecationLevel.WARNING)
public object OmniCreativeTabs {
    @JvmStatic
    @JvmOverloads
    public fun create(
        id: Identifier,
        icon: () -> Item,
        titleKey: String = OmniCreativeTab.DEFAULT_TITLE_KEY.format(id.namespace, id.path)
    ): OmniCreativeTab {
        return OmniCreativeTabImpl(id, icon, titleKey)
    }
}
