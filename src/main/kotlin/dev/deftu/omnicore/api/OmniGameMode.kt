package dev.deftu.omnicore.api

import dev.deftu.omnicore.api.commands.CommandCompletable

//#if MC >= 1.12.2
import net.minecraft.world.level.GameType
//#else
//$$ import net.minecraft.world.WorldSettings.GameType
//#endif

public enum class OmniGameMode(
    public val index: Int,
    override val id: String,
    public val aliases: List<String>,
) : CommandCompletable {
    ADVENTURE(2, "adventure", listOf("a")),
    CREATIVE(1, "creative", listOf("c")),
    SPECTATOR(3, "spectator", listOf("sp")),
    SURVIVAL(0, "survival", listOf("s")),
    UNKNOWN(-1, "", emptyList());

    public val vanilla: GameType
        get() = when (this) {
            ADVENTURE -> GameType.ADVENTURE
            CREATIVE -> GameType.CREATIVE
            SPECTATOR -> GameType.SPECTATOR
            SURVIVAL -> GameType.SURVIVAL

            //#if MC >= 1.17.1
            UNKNOWN -> GameType.DEFAULT_MODE
            //#else
            //$$ UNKNOWN -> GameType.NOT_SET
            //#endif
        }

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniGameMode> = values().toList()

        @JvmField
        public val COMPLETABLE: List<OmniGameMode> = ALL.filter { it.id.isNotEmpty() }

        @JvmField
        public val BY_INDEX: Map<Int, OmniGameMode> = ALL.associateBy(OmniGameMode::index)

        @JvmStatic
        public fun from(index: Int): OmniGameMode? {
            return BY_INDEX[index]
        }

        @JvmStatic
        public fun from(string: String): OmniGameMode? {
            return COMPLETABLE.firstOrNull { mode ->
                mode.id.equals(string, ignoreCase = true) ||
                mode.aliases.any { alias -> alias.equals(string, ignoreCase = true) }
            }
        }
    }
}
