package dev.deftu.omnicore.api.commands.types

import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.commands.types.enumerable.EnumArgumentType

public class GameModeArgumentType : EnumArgumentType<OmniGameMode>(OmniGameMode.COMPLETABLE.toTypedArray()) {
    public companion object {
        @JvmStatic
        public fun gameMode(): GameModeArgumentType {
            return GameModeArgumentType()
        }
    }
}
