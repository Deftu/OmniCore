package dev.deftu.omnicore.api.commands.types.gamemode

import com.mojang.brigadier.StringReader
import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.commands.ArgumentTypeVariant

public object IndexedModeVariant : ArgumentTypeVariant<OmniGameMode> {
    override fun parse(reader: StringReader): OmniGameMode? {
        return OmniGameMode.from(reader.readInt())
    }
}
