package dev.deftu.omnicore.api.client.options

import dev.deftu.omnicore.api.annotations.VersionedAbove
import net.minecraft.sound.SoundCategory as VanillaSoundCategory

public interface SoundCategory {
    public data object Master : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.MASTER
    }

    public data object Music : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.MUSIC
    }

    public data object Records : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.RECORDS
    }

    public data object Weather : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.WEATHER
    }

    public data object Blocks : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.BLOCKS
    }

    public data object HostileMobs : SoundCategory {
        //#if MC >= 1.12.2
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.HOSTILE
        //#else
        //$$ override val vanilla: VanillaSoundCategory = VanillaSoundCategory.MOBS
        //#endif
    }

    public data object NeutralMobs : SoundCategory {
        //#if MC >= 1.12.2
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.NEUTRAL
        //#else
        //$$ override val vanilla: VanillaSoundCategory = VanillaSoundCategory.ANIMALS
        //#endif
    }

    public data object Players : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.PLAYERS
    }

    public data object Ambient : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.AMBIENT
    }

    //#if MC >= 1.12.2
    @VersionedAbove("1.12.2")
    public data object Voices : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.VOICE
    }
    //#endif

    //#if MC >= 1.21.6
    @VersionedAbove("1.21.6")
    public data object UI : SoundCategory {
        override val vanilla: VanillaSoundCategory = VanillaSoundCategory.UI
    }
    //#endif

    public val vanilla: VanillaSoundCategory
}
