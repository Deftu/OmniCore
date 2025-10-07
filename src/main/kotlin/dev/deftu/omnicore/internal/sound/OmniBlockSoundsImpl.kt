package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniBlockSounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sound.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniBlockSoundsImpl : OmniBlockSounds {
    override val anvilUse: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_ANVIL_USE))
    }

    override val anvilBreak: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_ANVIL_BREAK))
    }

    override val anvilFall: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_ANVIL_FALL))
    }

    override val anvilLand: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_ANVIL_LAND))
    }

    override val anvilPlace: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_ANVIL_PLACE))
    }

    override val doorOpen: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_WOODEN_DOOR_OPEN))
    }

    override val doorClose: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_WOODEN_DOOR_CLOSE))
    }

    override val chestOpen: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_CHEST_OPEN))
    }

    override val chestClose: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.BLOCK_CHEST_CLOSE))
    }
}
