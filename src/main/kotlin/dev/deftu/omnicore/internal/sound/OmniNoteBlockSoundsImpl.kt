package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.sound.OmniNoteBlockSounds
import dev.deftu.omnicore.api.sound.OmniSound
import net.minecraft.sounds.SoundEvents
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniNoteBlockSoundsImpl : OmniNoteBlockSounds {
    override val basedrum: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_BASEDRUM))
    }

    override val bass: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_BASS))
    }

    override val harp: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_HARP))
    }

    override val hat: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_HAT))
    }

    override val pling: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_PLING))
    }

    override val snare: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_SNARE))
    }

    override val bell: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_BELL))
    }

    override val chime: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_CHIME))
    }

    override val flute: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_FLUTE))
    }

    override val guitar: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_GUITAR))
    }

    override val xylophone: OmniSound by lazy {
        OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_XYLOPHONE))
    }

    override val ironXylophone: OmniSound by lazy {
        OmniSound.invalidOr(
            //#if MC >= 1.16.5
            OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE))
            //#endif
        )
    }

    override val cowBell: OmniSound by lazy {
        OmniSound.invalidOr(
            //#if MC >= 1.16.5
            OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_COW_BELL))
            //#endif
        )
    }

    override val didgeridoo: OmniSound by lazy {
        OmniSound.invalidOr(
            //#if MC >= 1.16.5
            OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_DIDGERIDOO))
            //#endif
        )
    }

    override val bit: OmniSound by lazy {
        OmniSound.invalidOr(
            //#if MC >= 1.16.5
            OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_BIT))
            //#endif
        )
    }

    override val banjo: OmniSound by lazy {
        OmniSound.invalidOr(
            //#if MC >= 1.16.5
            OmniSound.of(unwrapSoundEvent(SoundEvents.NOTE_BLOCK_BANJO))
            //#endif
        )
    }
}
