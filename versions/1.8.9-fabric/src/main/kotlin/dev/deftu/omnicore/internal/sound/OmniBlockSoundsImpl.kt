package dev.deftu.omnicore.internal.sound

import dev.deftu.omnicore.api.identifierOrThrow
import dev.deftu.omnicore.api.sound.OmniBlockSounds
import dev.deftu.omnicore.api.sound.OmniSound
import org.jetbrains.annotations.ApiStatus

@ApiStatus.Internal
public object OmniBlockSoundsImpl : OmniBlockSounds {
    override val anvilUse: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.anvil_use"))
    }

    override val anvilBreak: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.anvil_break"))
    }

    override val anvilFall: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.anvil_land"))
    }

    override val anvilLand: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.anvil_fall"))
    }

    override val anvilPlace: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("dig.stone"))
    }

    override val doorOpen: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.door_open"))
    }

    override val doorClose: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.door_close"))
    }

    override val chestOpen: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.chestopen"))
    }

    override val chestClose: OmniSound by lazy {
        OmniSound.of(identifierOrThrow("random.chestclosed"))
    }
}
