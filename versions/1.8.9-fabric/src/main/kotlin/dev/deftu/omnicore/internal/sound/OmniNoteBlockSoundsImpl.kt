package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.sound.OmniNoteBlockSounds
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniNoteBlockSoundsImpl : OmniNoteBlockSounds {
    override val basedrum: OmniSound by lazy {
        OmniSound.of(locationOrThrow("note.bd"))
    }

    override val bass: OmniSound by lazy {
        OmniSound.of(locationOrThrow("note.bass"))
    }

    override val harp: OmniSound by lazy {
        OmniSound.of(locationOrThrow("note.harp"))
    }

    override val hat: OmniSound by lazy {
        OmniSound.of(locationOrThrow("note.hat"))
    }

    override val pling: OmniSound by lazy {
        OmniSound.of(locationOrThrow("note.pling"))
    }

    override val snare: OmniSound by lazy {
        OmniSound.of(locationOrThrow("note.snare"))
    }

    override val bell: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val chime: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val flute: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val guitar: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val xylophone: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val ironXylophone: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val cowBell: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val didgeridoo: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val bit: OmniSound by lazy {
        OmniSound.invalid()
    }

    override val banjo: OmniSound by lazy {
        OmniSound.invalid()
    }
}
