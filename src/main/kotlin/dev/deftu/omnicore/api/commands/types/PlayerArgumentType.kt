package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import net.minecraft.server.level.ServerPlayer

public class PlayerArgumentType : ArgumentType<ServerPlayer> {
    public companion object {
        @JvmStatic
        public fun one(): PlayerArgumentType {
            return PlayerArgumentType()
        }

        @JvmStatic
        public fun many(): PlayerArgumentType {
            return PlayerArgumentType()
        }
    }

    override fun parse(p0: StringReader?): ServerPlayer? {
        TODO("Not yet implemented")
    }

    override fun getExamples(): Collection<String?>? {
        return super.getExamples()
    }
}
