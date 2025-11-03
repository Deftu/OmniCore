package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CommonWolfSounds {
    public val shake: Lazy<OmniSound> = lazy {
        OmniSound.of(locationOrThrow("mob.wolf.shake"))
    }

    public val step: Lazy<OmniSound> = lazy {
        OmniSound.of(locationOrThrow("mob.wolf.step"))
    }
}
