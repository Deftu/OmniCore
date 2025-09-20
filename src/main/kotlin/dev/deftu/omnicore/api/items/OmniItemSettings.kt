package dev.deftu.omnicore.api.items

import net.minecraft.item.Item

//#if MC >= 1.16.5
import net.minecraft.component.type.FoodComponent
//#else
//$$ import net.minecraft.item.EnumAction
//$$ import net.minecraft.item.ItemFood
//$$ import net.minecraft.item.ItemStack
//#endif

@Deprecated(message = "Unstable API. Do not use until further notice.", level = DeprecationLevel.WARNING)
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
        //#if MC >= 1.16.5
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
                        //#if MC >= 1.21.2
                        food.consumableType.component
                        //#endif
                    )
                }
            }
        ).also { item ->
            tab?.append(item)
        }
        //#else
        //$$ val item = if (food != null) {
        //$$     val isDrink = food.consumableType is OmniConsumableType.Drink
        //$$     object : ItemFood(food.nutrition, food.saturation, false) {
        //$$         override fun getItemUseAction(stack: ItemStack): EnumAction? {
        //$$             return if (isDrink) EnumAction.DRINK else EnumAction.EAT
        //$$         }
        //$$     }.apply {
        //$$         if (food.isAlwaysEdible) {
        //$$             setAlwaysEdible()
        //$$         }
        //$$     }
        //$$ } else {
        //$$     Item()
        //$$ }.apply {
        //$$     setMaxStackSize(this@OmniItemSettings.maxStackSize)
        //$$     if (this@OmniItemSettings.maxDurability > 0) {
                //#if FORGE
                //$$ setMaxDamage(this@OmniItemSettings.maxDurability)
                //#else
                //$$ val method = Item::class.java.getDeclaredMethod("setMaxDamage", Int::class.javaPrimitiveType)
                //$$ method.isAccessible = true
                //$$ method.invoke(this, this@OmniItemSettings.maxDurability)
                //#endif
        //$$     }
        //$$ }
        //$$
        //$$ return item.also { item ->
        //$$     tab?.append(item)
        //$$ }
        //#endif
    }
}
