package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.internal.internalLocationOf
import net.minecraft.resources.ResourceLocation

//#if MC >= 1.12.2
import net.minecraft.sounds.SoundEvent
//#endif

public class OmniSound {
    public companion object {
        @JvmStatic
        public fun of(location: ResourceLocation): OmniSound {
            return OmniSound(location)
        }

        @JvmStatic
        public fun of(path: String): OmniSound {
            return OmniSound(locationOrThrow(path))
        }

        @JvmStatic
        public fun of(namespace: String, path: String): OmniSound {
            return OmniSound(locationOrThrow(namespace, path))
        }

        //#if MC >= 1.12.2
        @JvmStatic
        public fun of(event: SoundEvent): OmniSound {
            return OmniSound(event)
        }
        //#endif

        @JvmStatic
        public fun invalid(): OmniSound {
            return OmniSound(internalLocationOf("invalid"))
        }

        @JvmStatic
        public fun invalidOr(value: OmniSound? = null): OmniSound {
            return value ?: invalid()
        }
    }

    public val location: ResourceLocation

    //#if MC >= 1.12.2
    public val event: SoundEvent
    //#endif

    private constructor(location: ResourceLocation) {
        this.location = location
        //#if MC >= 1.12.2
        //#if MC >= 1.19.4
        this.event = SoundEvent.createVariableRangeEvent(location)
        //#else
        //$$ this.event = SoundEvent(location)
        //#endif
        //#endif
    }

    //#if MC >= 1.12.2
    private constructor(event: SoundEvent) {
        this.location = event.location
        this.event = event
    }

    public fun isSameEvent(other: SoundEvent): Boolean {
        return this.event == other
    }
    //#endif

    public fun isSameLocation(location: ResourceLocation): Boolean {
        return this.location == location
    }

    override fun equals(other: Any?): Boolean {
        return this === other || (other is OmniSound && this.location == other.location)
    }

    /**
     * Returns the hash code of this sound's resource location. We can't consider the sound event's hash code because it's only available in 1.12.2 and above.
     */
    override fun hashCode(): Int {
        return location.hashCode()
    }
}
