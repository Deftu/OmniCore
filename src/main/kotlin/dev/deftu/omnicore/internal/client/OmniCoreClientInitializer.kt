package dev.deftu.omnicore.internal.client

import dev.deftu.omnicore.api.GIT_BRANCH
import dev.deftu.omnicore.api.GIT_COMMIT
import dev.deftu.omnicore.api.GIT_URL
import dev.deftu.omnicore.api.VERSION
import dev.deftu.omnicore.api.client.commands.OmniClientCommands
import dev.deftu.omnicore.api.client.commands.command
import dev.deftu.omnicore.api.client.input.keybindings.MCKeyBinding
import dev.deftu.omnicore.api.client.render.ImmediateScreenRenderer
import dev.deftu.omnicore.api.client.render.OmniFrameClock
import dev.deftu.omnicore.api.client.render.OmniRenderTicks
import dev.deftu.omnicore.internal.client.commands.ClientCommandInternals
import dev.deftu.omnicore.internal.client.events.ClientEventForwarding
import dev.deftu.omnicore.internal.client.networking.ClientNetworkingInternals
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.ClickEvent
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import org.apache.logging.log4j.LogManager
import org.jetbrains.annotations.ApiStatus
import java.net.URI

@ApiStatus.Internal
public object OmniCoreClientInitializer {
    private val logger = LogManager.getLogger(OmniCoreClientInitializer::class.java)

    public var isInitialized: Boolean = false
        private set

    public fun initialize() {
        if (isInitialized) {
            return
        }

        logger.info("Initializing OmniCore client $VERSION")

        ClientEventForwarding.initialize()
        ImmediateScreenRenderer.initialize()
        MCKeyBinding.initialize()
        OmniFrameClock.initialize()
        OmniRenderTicks.initialize()
        ClientCommandInternals.initialize()
        ClientNetworkingInternals.initialize()

        isInitialized = true
    }

    public fun commands() {
        OmniClientCommands.command("omnicore") {
            then("version") {
                runs { ctx ->
                    val gitPart = Text.literal("($GIT_BRANCH/$GIT_COMMIT)")
                        .setStyle(MCTextStyle.color(TextColors.BLUE).italic().setClickEvent(ClickEvent.OpenUrl(URI.create(GIT_URL))))
                    val message = Text.literal("OmniCore v$VERSION ")
                        .setStyle(MCTextStyle.color(TextColors.GREEN))
                        .append(gitPart)

                    ctx.source.replyChat(message)
                }
            }

            then("mau5") {
                runs { ctx ->
                    val lyrics = mutableListOf<Text>()
                    lyrics.add(Text.literal("I like the sound of the broken pieces", MCTextStyle.color(TextColors.DARK_PURPLE)))
                    lyrics.add(Text.literal("I like the lights and the siren, she says", MCTextStyle.color(TextColors.LIGHT_PURPLE)))
                    lyrics.add(Text.literal("We got machines but the kids got Jesus", MCTextStyle.color(TextColors.LIGHT_PURPLE)))
                    lyrics.add(Text.literal("We like to move like we both don't need this", MCTextStyle.color(TextColors.DARK_PURPLE)))
                    lyrics.add(Text.literal("God can't hear you", MCTextStyle.color(TextColors.GRAY).italic()))
                    lyrics.add(Text.literal("They won't fight you", MCTextStyle.color(TextColors.GRAY).italic()))
                    lyrics.add(Text.literal("Watch them build a friend just like you", MCTextStyle.color(TextColors.GRAY).italic()))
                    lyrics.add(Text.literal("Morning sickness, XYZ", MCTextStyle.color(TextColors.DARK_PURPLE)))
                    lyrics.add(Text.literal("Teenage girls with ESP", MCTextStyle.color(TextColors.LIGHT_PURPLE)))
                    lyrics.add(Text.literal("Give me the sound to see", MCTextStyle.color(TextColors.GOLD).bold().italic()))
                    lyrics.add(Text.literal("Another world outside that's full of", MCTextStyle.color(TextColors.GOLD).bold().italic()))
                    lyrics.add(Text.literal("All the broken things that I made", MCTextStyle.color(TextColors.GOLD).bold().italic()))
                    lyrics.add(Text.literal("Just give me a life to bleed", MCTextStyle.color(TextColors.GOLD).bold().italic()))
                    lyrics.add(Text.literal("Another world outside that's full of", MCTextStyle.color(TextColors.GOLD).bold().italic()))
                    lyrics.add(Text.literal("All the awful things that I made", MCTextStyle.color(TextColors.GOLD).bold().italic()))

                    val message = Text.join("\n", lyrics)
                        .setStyle(MCTextStyle().setClickEvent(ClickEvent.OpenUrl(URI.create("https://youtu.be/Hr2Bc5qMhE4"))))

                    ctx.source.replyChat(message)
                }
            }
        }.register()
    }
}
