package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniEntitySounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sounds.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniEntitySoundsImpl : OmniEntitySounds {
    override val experienceOrb: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.EXPERIENCE_ORB_PICKUP))
    }

    override val levelUp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.PLAYER_LEVELUP))
    }

    override val itemPickUp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ITEM_PICKUP))
    }

    override val itemBreak: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ITEM_BREAK))
    }
}
