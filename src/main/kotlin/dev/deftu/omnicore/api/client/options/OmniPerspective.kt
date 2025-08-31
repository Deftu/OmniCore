package dev.deftu.omnicore.api.client.options

import dev.deftu.omnicore.client.OmniClient

//#if MC >= 1.16.5
import net.minecraft.client.option.Perspective
//#endif

/**
 * Represents the camera perspectives available in Minecraft
 * and provides utilities to read and apply them via the client options.
 *
 * @property isFirstPerson true if this perspective is first-person
 * @property isFrontView  true if this perspective shows the camera in front of the player
 */
@Suppress("EnumValuesSoftDeprecate")
public enum class OmniPerspective(
    public val isFirstPerson: Boolean,
    public val isFrontView: Boolean
) {

    FIRST_PERSON(true,  false),
    THIRD_PERSON_BACK(false, false),
    THIRD_PERSON_FRONT(false, true);

    public val isThirdPerson: Boolean get() = !isFirstPerson

    /**
     * Sets the current camera perspective to this.
     */
    public fun apply() {
        rawCurrentPerspective = ordinal
    }

    /**
     * Cycles to the next perspective, wrapping back to FIRST_PERSON.
     */
    public fun next(): OmniPerspective {
        return ALL[(ordinal + 1) % ALL.size]
    }

    /**
     * Cycles to the previous perspective, wrapping back to THIRD_PERSON_FRONT.
     */
    public fun previous(): OmniPerspective {
        return ALL[(ordinal - 1 + ALL.size) % ALL.size]
    }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniPerspective> = values().toList()

        @JvmField
        public val FRONT_VIEW: Set<OmniPerspective> = setOf(FIRST_PERSON, THIRD_PERSON_FRONT)

        @JvmField
        public val BACK_VIEW: Set<OmniPerspective> = setOf(THIRD_PERSON_BACK)

        @JvmField
        public val FIRST_PERSON_VIEW: Set<OmniPerspective> = setOf(FIRST_PERSON)

        @JvmField
        public val THIRD_PERSON_VIEW: Set<OmniPerspective> = setOf(THIRD_PERSON_BACK, THIRD_PERSON_FRONT)

        /**
         * The raw integer value of the current camera perspective
         * as provided by the Minecraft client.
         */
        @JvmStatic
        public var rawCurrentPerspective: Int
            get() {
                //#if MC >= 1.16.2
                return OmniClient.getInstance().options.perspective.ordinal
                //#else
                //$$ return OmniClient.getInstance().gameSettings.thirdPersonView
                //#endif
            }
            set(value) {
                //#if MC >= 1.16.2
                OmniClient.getInstance().options.perspective = Perspective.values()[value]
                //#else
                //$$ OmniClient.getInstance().gameSettings.thirdPersonView = value
                //#endif
            }

        /**
         * The current camera perspective.
         */
        @JvmStatic
        public var currentPerspective: OmniPerspective
            get() = from(rawCurrentPerspective)
            set(value) { rawCurrentPerspective = value.ordinal }

        /**
         * Returns an OmniPerspective for the given raw index.
         */
        @JvmStatic
        public fun from(value: Int): OmniPerspective {
            return ALL[value]
        }
    }

}