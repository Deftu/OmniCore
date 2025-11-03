package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniBlockSounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sounds.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniBlockSoundsImpl : OmniBlockSounds {
    override val anvilUse: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ANVIL_USE))
    }

    override val anvilBreak: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ANVIL_BREAK))
    }

    override val anvilFall: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ANVIL_FALL))
    }

    override val anvilLand: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ANVIL_LAND))
    }

    override val anvilPlace: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.ANVIL_PLACE))
    }

    override val doorOpen: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOODEN_DOOR_OPEN))
    }

    override val doorClose: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.WOODEN_DOOR_CLOSE))
    }

    override val chestOpen: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.CHEST_OPEN))
    }

    override val chestClose: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.CHEST_CLOSE))
    }
}
