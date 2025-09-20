package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.internal.identifierOf
import net.minecraft.util.Identifier

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#endif

public class OmniSound {
    public companion object {
        @JvmStatic
        public fun invalid(): OmniSound {
            return OmniSound(identifierOf("invalid"))
        }
    }

    public val location: Identifier

    //#if MC >= 1.12.2
    public val event: SoundEvent
    //#endif

    public constructor(location: Identifier) {
        this.location = location
        //#if MC >= 1.12.2
        //#if MC >= 1.19.4
        this.event = SoundEvent.of(location)
        //#else
        //$$ this.event = SoundEvent(location)
        //#endif
        //#endif
    }

    //#if MC >= 1.12.2
    public constructor(event: SoundEvent) {
        this.location = event.id()
        this.event = event
    }
    //#endif

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other is Identifier -> location == other
            //#if MC >= 1.12.2
            other is SoundEvent -> event == other
            //#endif
            other is OmniSound -> location == other.location
            else -> false
        }
    }

    /**
     * Returns the hash code of this sound's identifier. We can't consider the sound event's hash code because it's only available in 1.12.2 and above.
     */
    override fun hashCode(): Int {
        return location.hashCode()
    }
}
