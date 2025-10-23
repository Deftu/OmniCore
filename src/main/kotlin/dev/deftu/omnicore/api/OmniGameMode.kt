package dev.deftu.omnicore.api

import dev.deftu.omnicore.api.commands.CommandCompletable

public enum class OmniGameMode(override val id: String) : CommandCompletable {
    ADVENTURE("adventure"),
    CREATIVE("creative"),
    SPECTATOR("spectator"),
    SURVIVAL("survival"),
    UNKNOWN("");

    public companion object {
        @JvmField
        @Suppress("EnumValuesSoftDeprecate")
        public val ALL: List<OmniGameMode> = values().toList()

        @JvmField
        public val COMPLETABLE: List<OmniGameMode> = ALL.filter { it.id.isNotEmpty() }
    }
}
