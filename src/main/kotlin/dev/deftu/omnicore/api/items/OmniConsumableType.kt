package dev.deftu.omnicore.api.items

//#if MC >= 1.21.2
import net.minecraft.component.type.ConsumableComponent
import net.minecraft.component.type.ConsumableComponents
//#endif

public sealed interface OmniConsumableType {
    public data object Food : OmniConsumableType {
        //#if MC >= 1.21.2
        override val component: ConsumableComponent = ConsumableComponents.FOOD
        //#endif
    }

    public data object Drink : OmniConsumableType {
        //#if MC >= 1.21.2
        override val component: ConsumableComponent = ConsumableComponents.DRINK
        //#endif
    }

    //#if MC >= 1.21.2
    public data class Custom(override val component: ConsumableComponent) : OmniConsumableType
    //#endif

    //#if MC >= 1.21.2
    public val component: ConsumableComponent
    //#endif
}
