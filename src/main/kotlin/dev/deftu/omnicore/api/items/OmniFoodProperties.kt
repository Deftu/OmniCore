package dev.deftu.omnicore.api.items

@Deprecated(message = "Unstable API. Do not use until further notice.", level = DeprecationLevel.WARNING)
public data class OmniFoodProperties(
    public val nutrition: Int,
    public val saturation: Float,
    public val isAlwaysEdible: Boolean = false,
    public val isMeat: Boolean = false,
    public val consumableType: OmniConsumableType = OmniConsumableType.Food,
) {
    public class Builder(public var nutrition: Int, public var saturation: Float) {
        public var isAlwaysEdible: Boolean = false
        public var isMeat: Boolean = false
        public var consumableType: OmniConsumableType = OmniConsumableType.Food

        public fun alwaysEdible(): Builder {
            this.isAlwaysEdible = true
            return this
        }

        public fun meat(): Builder {
            this.isMeat = true
            return this
        }

        public fun consumableType(type: OmniConsumableType): Builder {
            this.consumableType = type
            return this
        }

        public fun build(): OmniFoodProperties {
            return OmniFoodProperties(
                nutrition = nutrition,
                saturation = saturation,
                isAlwaysEdible = isAlwaysEdible,
                isMeat = isMeat,
                consumableType = consumableType,
            )
        }
    }
}
