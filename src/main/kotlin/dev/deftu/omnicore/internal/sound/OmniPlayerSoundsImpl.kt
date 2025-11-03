package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniPlayerSounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sounds.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniPlayerSoundsImpl : OmniPlayerSounds {
    override val hurt: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_HURT))
    }

    override val death: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_DEATH))
    }

    override val swim: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_SWIM))
    }

    override val splash: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_SPLASH))
    }

    override val burp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_BURP))
    }

    override val fallBig: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_BIG_FALL))
    }

    override val fallSmall: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_SMALL_FALL))
    }
}
