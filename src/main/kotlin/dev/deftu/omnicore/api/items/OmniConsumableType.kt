package dev.deftu.omnicore.api.items

//#if MC >= 1.19.3
import net.minecraft.component.type.ConsumableComponent
import net.minecraft.component.type.ConsumableComponents
//#endif

public sealed interface OmniConsumableType {
    public data object Food : OmniConsumableType {
        override val component: ConsumableComponent = ConsumableComponents.FOOD
    }

    public data object Drink : OmniConsumableType {
        override val component: ConsumableComponent = ConsumableComponents.DRINK
    }

    //#if MC >= 1.19.3
    public data class Custom(override val component: ConsumableComponent) : OmniConsumableType
    //#endif

    public val component: ConsumableComponent
}
