package dev.deftu.omnicore.client

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.client.OmniClientCommands.command
import dev.deftu.omnicore.client.OmniClientCommands.does
import dev.deftu.omnicore.client.OmniClientCommands.register
import dev.deftu.omnicore.client.events.OmniClientEventPassthrough
import dev.deftu.omnicore.client.keybindings.MCKeyBinding
import dev.deftu.omnicore.client.render.ImmediateScreenRenderer
import dev.deftu.omnicore.client.render.OmniGameRendering
import dev.deftu.textile.TextHolder
import dev.deftu.textile.minecraft.MCClickEvent
import dev.deftu.textile.minecraft.MCHoverEvent
import dev.deftu.textile.minecraft.MCSimpleMutableTextHolder
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextFormat
import org.apache.logging.log4j.LogManager
import java.net.URI

public object OmniCoreClient {
    private val logger = LogManager.getLogger(OmniCoreClient::class.java)

    public var isInitialized: Boolean = false
        private set

    public fun initialize() {
        if (isInitialized) {
            return
        }

        logger.info("Initializing OmniCore client-side ${OmniCore.VERSION}")

        OmniClientEventPassthrough.initialize()
        ImmediateScreenRenderer.initialize()
        OmniClientCommands.initialize()
        OmniGameRendering.initialize()
        MCKeyBinding.initialize()

        OmniClientCommands.command(OmniCore.ID) {
            does {
                source.displayMessage(MCSimpleTextHolder("Hello, OmniCore!").withFormatting(MCTextFormat.LIGHT_PURPLE))
                1
            }

            command("version") {
                does {
                    val clickEvent = MCClickEvent.OpenUrl(URI.create(OmniCore.GIT_URL))
                    val hoverEvent = MCHoverEvent.ShowText(OmniCore.GIT_URL)

                    val lines = listOf(
                        MCSimpleMutableTextHolder("OmniCore ")
                            .setFormatting(MCTextFormat.GOLD, MCTextFormat.BOLD)
                            .append(
                                MCSimpleTextHolder("(${OmniCore.ID})")
                                    .withFormatting(MCTextFormat.GREEN)
                            ),
                        MCSimpleMutableTextHolder("Version: ")
                            .setFormatting(MCTextFormat.GOLD, MCTextFormat.BOLD)
                            .append(
                                MCSimpleTextHolder(OmniCore.VERSION)
                                    .withFormatting(MCTextFormat.GREEN)
                            ),
                        MCSimpleMutableTextHolder("Branch: ")
                            .setFormatting(MCTextFormat.GOLD, MCTextFormat.BOLD)
                            .setClickEvent(clickEvent)
                            .setHoverEvent(hoverEvent)
                            .append(
                                MCSimpleTextHolder(OmniCore.GIT_BRANCH)
                                    .withFormatting(MCTextFormat.GREEN)
                            ),
                        MCSimpleMutableTextHolder("Commit: ")
                            .setFormatting(MCTextFormat.GOLD, MCTextFormat.BOLD)
                            .setClickEvent(clickEvent)
                            .setHoverEvent(hoverEvent)
                            .append(
                                MCSimpleTextHolder(OmniCore.GIT_COMMIT)
                                    .withFormatting(MCTextFormat.GREEN)
                            ),
                    )

                    val maxLength = lines.map(TextHolder<*, *>::asUnformattedString).maxOf(String::length)

                    source.displayMessage("-".repeat(maxLength))
                    lines.forEach(source::displayMessage)
                    source.displayMessage("-".repeat(maxLength))

                    1
                }
            }
        }.register()

        isInitialized = true
    }
}
