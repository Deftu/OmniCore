package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side

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
@GameSide(Side.CLIENT)
@Suppress("EnumValuesSoftDeprecate")
public enum class OmniPerspective(
    @get:GameSide(Side.CLIENT)
    public val isFirstPerson: Boolean,

    @get:GameSide(Side.CLIENT)
    public val isFrontView: Boolean
) {

    FIRST_PERSON(true,  false),
    THIRD_PERSON_BACK(false, false),
    THIRD_PERSON_FRONT(false, true);

    public val isThirdPerson: Boolean get() = !isFirstPerson

    /**
     * Sets the current camera perspective to this.
     */
    @GameSide(Side.CLIENT)
    public fun apply() {
        rawCurrentPerspective = ordinal
    }

    /**
     * Cycles to the next perspective, wrapping back to FIRST_PERSON.
     */
    @GameSide(Side.CLIENT)
    public fun next(): OmniPerspective {
        return ALL[(ordinal + 1) % ALL.size]
    }

    /**
     * Cycles to the previous perspective, wrapping back to THIRD_PERSON_FRONT.
     */
    @GameSide(Side.CLIENT)
    public fun previous(): OmniPerspective {
        return ALL[(ordinal - 1 + ALL.size) % ALL.size]
    }

    public companion object {

        @JvmField
        @GameSide(Side.BOTH)
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniPerspective> = OmniPerspective.values().toList()

        @JvmField
        @GameSide(Side.BOTH)
        public val FRONT_VIEW: Set<OmniPerspective> = setOf(FIRST_PERSON, THIRD_PERSON_FRONT)

        @JvmField
        @GameSide(Side.BOTH)
        public val BACK_VIEW: Set<OmniPerspective> = setOf(THIRD_PERSON_BACK)

        @JvmField
        @GameSide(Side.BOTH)
        public val FIRST_PERSON_VIEW: Set<OmniPerspective> = setOf(FIRST_PERSON)

        @JvmField
        @GameSide(Side.BOTH)
        public val THIRD_PERSON_VIEW: Set<OmniPerspective> = setOf(THIRD_PERSON_BACK, THIRD_PERSON_FRONT)

        /**
         * The raw integer value of the current camera perspective
         * as provided by the Minecraft client.
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
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
        @GameSide(Side.CLIENT)
        public var currentPerspective: OmniPerspective
            get() = fromInt(rawCurrentPerspective)
            set(value) { rawCurrentPerspective = value.ordinal }

        /**
         * Returns an OmniPerspective for the given raw index.
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromInt(value: Int): OmniPerspective {
            return ALL[value]
        }

    }

}
