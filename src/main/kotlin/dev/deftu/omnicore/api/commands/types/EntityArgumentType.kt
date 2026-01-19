package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import net.minecraft.world.entity.Entity

public class EntityArgumentType : ArgumentType<Entity> {
    public companion object {
        @JvmStatic
        public fun one(): EntityArgumentType {
            return EntityArgumentType()
        }

        @JvmStatic
        public fun many(): EntityArgumentType {
            return EntityArgumentType()
        }
    }

    override fun parse(p0: StringReader?): Entity? {
        TODO("Not yet implemented")
    }

    override fun getExamples(): Collection<String?>? {
        return super.getExamples()
    }
}
