package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.internal.identifierOf
import net.minecraft.util.Identifier

//#if MC >= 1.12.2
import net.minecraft.sound.SoundEvent
//#endif

public class OmniSound {
    public companion object {
        @JvmStatic
        public fun of(location: Identifier): OmniSound {
            return OmniSound(location)
        }

        @JvmStatic
        public fun of(path: String): OmniSound {
            return OmniSound(identifierOrThrow(path))
        }

        @JvmStatic
        public fun of(namespace: String, path: String): OmniSound {
            return OmniSound(identifierOrThrow(namespace, path))
        }

        //#if MC >= 1.12.2
        @JvmStatic
        public fun of(event: SoundEvent): OmniSound {
            return OmniSound(event)
        }
        //#endif

        @JvmStatic
        public fun invalid(): OmniSound {
            return OmniSound(identifierOf("invalid"))
        }

        @JvmStatic
        public fun invalidOr(value: OmniSound? = null): OmniSound {
            return value ?: invalid()
        }
    }

    public val location: Identifier

    //#if MC >= 1.12.2
    public val event: SoundEvent
    //#endif

    private constructor(location: Identifier) {
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
    private constructor(event: SoundEvent) {
        this.location = event.id()
        this.event = event
    }

    public fun isSameEvent(other: SoundEvent): Boolean {
        return this.event == other
    }
    //#endif

    public fun isSameLocation(location: Identifier): Boolean {
        return this.location == location
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is OmniSound && this.location == other.location)
    }

    /**
     * Returns the hash code of this sound's identifier. We can't consider the sound event's hash code because it's only available in 1.12.2 and above.
     */
    override fun hashCode(): Int {
        return location.hashCode()
    }
}
