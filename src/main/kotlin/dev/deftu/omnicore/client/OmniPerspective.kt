package dev.deftu.omnicore.client

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
//#if MC >= 1.16.2
import net.minecraft.client.option.Perspective
//#endif

/**
 * Represents the camera perspectives available in Minecraft
 * and provides utilities to read and apply them via the client options.
 *
 * @property firstPerson true if this perspective is first-person
 * @property frontView  true if this perspective shows the camera in front of the player
 */
@GameSide(Side.CLIENT)
public enum class OmniPerspective(
    @get:GameSide(Side.CLIENT)
    public val firstPerson: Boolean,

    @get:GameSide(Side.CLIENT)
    public val frontView: Boolean
) {
    FIRST_PERSON(true,  false),
    THIRD_PERSON_BACK(false, false),
    THIRD_PERSON_FRONT(false, true);

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
        val all = OmniPerspective.entries.toTypedArray()
        return all[(ordinal + 1) % all.size]
    }


    /**
     * Cycles to the previous perspective, wrapping back to THIRD_PERSON_FRONT.
     */
    @GameSide(Side.CLIENT)
    public fun prev(): OmniPerspective {
        val all = OmniPerspective.entries.toTypedArray()
        return all[(ordinal - 1 + all.size) % all.size]
    }

    public companion object {
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
                OmniClient.getInstance().options.perspective = Perspective.entries[value]
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
         * Returns an OmniPerspective for the given raw index
         */
        @JvmStatic
        @GameSide(Side.CLIENT)
        public fun fromInt(value: Int): OmniPerspective =
            OmniPerspective.entries[value]
    }
}