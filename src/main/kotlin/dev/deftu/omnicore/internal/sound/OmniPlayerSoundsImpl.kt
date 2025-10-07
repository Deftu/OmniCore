package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniPlayerSounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sound.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniPlayerSoundsImpl : OmniPlayerSounds {
    override val hurt: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_HURT))
    }

    override val death: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_DEATH))
    }

    override val swim: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_SWIM))
    }

    override val splash: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_SPLASH))
    }

    override val burp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_BURP))
    }

    override val fallBig: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_BIG_FALL))
    }

    override val fallSmall: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ENTITY_PLAYER_SMALL_FALL))
    }
}
