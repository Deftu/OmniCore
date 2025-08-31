package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.common.OmniIdentifier
import net.minecraft.util.Identifier

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#endif

public class OmniSound {
    public companion object {
        @JvmStatic
        public fun invalid(): OmniSound {
            return OmniSound(OmniIdentifier.create(OmniCore.ID, "invalid"))
        }
    }

    public val identifier: Identifier

    //#if MC >= 1.12.2
    public val event: SoundEvent
    //#endif

    public constructor(identifier: Identifier) {
        this.identifier = identifier
        //#if MC >= 1.12.2
        //#if MC >= 1.19.4
        this.event = SoundEvent.of(identifier)
        //#else
        //$$ this.event = SoundEvent(identifier)
        //#endif
        //#endif
    }

    //#if MC >= 1.12.2
    public constructor(event: SoundEvent) {
        this.identifier = event.id()
        this.event = event
    }
    //#endif

    override fun equals(other: Any?): Boolean {
        return when {
            this === other -> true
            other is Identifier -> identifier == other
            //#if MC >= 1.12.2
            other is SoundEvent -> event == other
            //#endif
            other is OmniSound -> identifier == other.identifier
            else -> false
        }
    }

    /**
     * Returns the hash code of this sound's identifier. We can't consider the sound event's hash code because it's only available in 1.12.2 and above.
     */
    override fun hashCode(): Int {
        return identifier.hashCode()
    }
}
