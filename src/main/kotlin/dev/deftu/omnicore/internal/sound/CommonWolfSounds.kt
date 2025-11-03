package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sounds.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CommonWolfSounds {
    public val shake: Lazy<OmniSound> = lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_SHAKE))
    }

    public val step: Lazy<OmniSound> = lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOLF_STEP))
    }
}
