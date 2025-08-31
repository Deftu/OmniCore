package dev.deftu.omnicore.api.items

import net.minecraft.component.type.FoodComponent
import net.minecraft.item.Item

public data class OmniItemSettings(
    val maxStackSize: Int = 64,
    val maxDurability: Int = 0,
    val isFireResistant: Boolean = false,
    val tab: OmniCreativeTab? = null,
    val food: OmniFoodProperties? = null,
) {
    public class Builder {
        public var maxStackSize: Int = 64
        public var maxDurability: Int = 0
        public var isFireResistant: Boolean = false
        public var food: OmniFoodProperties? = null
        public var tab: OmniCreativeTab? = null

        public fun maxStackSize(size: Int): Builder {
            this.maxStackSize = size
            return this
        }

        public fun maxDurability(durability: Int): Builder {
            this.maxDurability = durability
            return this
        }

        public fun fireResistant(): Builder {
            this.isFireResistant = true
            return this
        }

        public fun food(properties: OmniFoodProperties): Builder {
            this.food = properties
            return this
        }

        public fun tab(tab: OmniCreativeTab): Builder {
            this.tab = tab
            return this
        }

        public fun build(): OmniItemSettings {
            return OmniItemSettings(
                maxStackSize = maxStackSize,
                maxDurability = maxDurability,
                isFireResistant = isFireResistant,
                food = this@Builder.food,
                tab = tab,
            )
        }
    }

    public fun toBuilder(): Builder {
        return Builder()
            .maxStackSize(maxStackSize)
            .maxDurability(maxDurability)
            .apply { if (isFireResistant) fireResistant() }
            .apply { if (food != null) food(food!!) }
            .apply { if (tab != null) tab(tab!!) }
    }

    public fun create(): Item {
        //#if MC >= 1.19.4
        return Item(Item.Settings()
            .maxCount(maxStackSize)
            .maxDamage(if (maxDurability > 0) maxDurability else 0)
            .apply {
                if (isFireResistant) {
                    fireproof()
                }

                if (food != null) {
                    food(FoodComponent.Builder()
                        .nutrition(food.nutrition)
                        .saturationModifier(food.saturation)
                        .apply {
                            if (food.isAlwaysEdible) {
                                alwaysEdible()
                            }
                        }.build(),
                        food.consumableType.component
                    )
                }
            }
        ).also { item ->
            tab?.append(item)
        }
        //#else
        //$$ // TODO
        //#endif
    }
}
