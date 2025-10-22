package dev.deftu.omnicore.api.commands.types

import com.mojang.brigadier.StringReader
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.api.sound.OmniSounds
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.CompletableFuture

@ApiStatus.Experimental
public class OmniSoundArgumentType(private val permittedSounds: Set<OmniSound>) : ArgumentType<OmniSound> {
    public companion object {
        private val EXAMPLES = listOf(OmniSounds.PLAYER.burp, OmniSounds.PLAYER.hurt)
        private val EXCEPTION = SimpleCommandExceptionType { "Unknown sound" }

        @JvmStatic
        public fun sound(sound: OmniSound): OmniSoundArgumentType {
            return OmniSoundArgumentType(setOf(sound))
        }

        @JvmStatic
        public fun sounds(vararg sounds: OmniSound): OmniSoundArgumentType {
            return OmniSoundArgumentType(sounds.toSet())
        }

        @JvmStatic
        public fun sounds(sounds: Collection<OmniSound>): OmniSoundArgumentType {
            return OmniSoundArgumentType(sounds.toSet())
        }

        @JvmStatic
        public fun <T> getSound(context: CommandContext<T>, name: String): OmniSound {
            return context.getArgument(name, OmniSound::class.java)
        }

        private fun OmniSound.string(): String {
            return this.location.toString()
        }

        private fun StringReader.string(): String {
            val start = cursor
            while (canRead() && !peek().isWhitespace()) {
                skip()
            }

            return string.substring(start, cursor)
        }
    }

    override fun parse(reader: StringReader): OmniSound {
        val cursor = reader.cursor
        val value = reader.string()
        val sound = permittedSounds.firstOrNull { it.string() == value }
        if (sound == null) {
            reader.cursor = cursor
            throw EXCEPTION.createWithContext(reader)
        }

        return sound
    }

    override fun <S : Any?> listSuggestions(
        context: CommandContext<S?>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        for (sound in permittedSounds) {
            val key = sound.string()
            if (key.startsWith(builder.remaining)) {
                builder.suggest(key)
            }
        }

        return builder.buildFuture()
    }

    override fun getExamples(): Collection<String> {
        return EXAMPLES.map { sound ->
            sound.string()
        }
    }
}
