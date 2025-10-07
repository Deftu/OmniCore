package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sound.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object CommonWolfSounds {
    public val shake: Lazy<OmniSound> = lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_WOLF_SHAKE))
    }

    public val step: Lazy<OmniSound> = lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_WOLF_STEP))
    }
}
