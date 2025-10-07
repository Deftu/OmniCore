package dev.deftu.omnicore.api.sound

import dev.deftu.omnicore.internal.sound.OmniBlockSoundsImpl
import dev.deftu.omnicore.internal.sound.OmniNoteBlockSoundsImpl
import dev.deftu.omnicore.internal.sound.OmniEntitySoundsImpl
import dev.deftu.omnicore.internal.sound.OmniMiscellaneousSoundsImpl
import dev.deftu.omnicore.internal.sound.OmniPlayerSoundsImpl
import dev.deftu.omnicore.internal.sound.OmniWolfSoundsImpl

public object OmniSounds {
    @JvmField public val BLOCK: OmniBlockSounds = OmniBlockSoundsImpl
    @JvmField public val ENTITY: OmniEntitySounds = OmniEntitySoundsImpl
    @JvmField public val NOTE_BLOCK: OmniNoteBlockSounds = OmniNoteBlockSoundsImpl
    @JvmField public val PLAYER: OmniPlayerSounds = OmniPlayerSoundsImpl
    @JvmField public val WOLF: OmniWolfSounds = OmniWolfSoundsImpl
    @JvmField public val MISCELLANEOUS: OmniMiscellaneousSounds = OmniMiscellaneousSoundsImpl
}
