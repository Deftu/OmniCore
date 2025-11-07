package dev.deftu.omnicore.internal.chat

import dev.deftu.omnicore.api.chat.Audience
import dev.deftu.omnicore.api.chat.TitleInfo
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.textile.CollapseMode
import dev.deftu.textile.Text
import org.apache.logging.log4j.LogManager

public object ConsoleAudience : Audience {
    private val logger = LogManager.getLogger("OmniCore/Console Audience")

    override fun sendChat(text: Text) {
        logger.info("[Chat] " + text.collapseToString(CollapseMode.SCOPED))
    }

    override fun sendActionBar(text: Text) {
        logger.info("[ActionBar] " + text.collapseToString(CollapseMode.SCOPED))
    }

    override fun sendTitle(titleInfo: TitleInfo) {
        val timings = buildString {
            with(titleInfo.timings) {
                append("Fin: ${fadeIn}ts, ")
                append("Disp: ${stay}ts, ")
                append("Fout: ${fadeOut}ts")
            }
        }

        val message = buildString {
            append(titleInfo.title.collapseToString(CollapseMode.SCOPED))

            titleInfo.subtitle?.let { subtitle ->
                append(" | ")
                append(subtitle.collapseToString(CollapseMode.SCOPED))
            }
        }

        logger.info("[Title ($timings)] $message")
    }

    override fun playSound(sound: OmniSound, volume: Float, pitch: Float) {
        // no-op
    }
}
