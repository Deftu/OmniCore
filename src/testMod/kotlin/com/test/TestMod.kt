package com.test

import com.mojang.brigadier.arguments.StringArgumentType
import dev.deftu.eventbus.on
import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.client.*
import dev.deftu.omnicore.client.OmniClientCommands.argument
import dev.deftu.omnicore.client.OmniClientCommands.command
import dev.deftu.omnicore.client.OmniClientCommands.does
import dev.deftu.omnicore.client.OmniClientCommands.register
import dev.deftu.omnicore.client.events.ScreenEvent
import dev.deftu.omnicore.client.keybindings.OmniKeyBinding
import dev.deftu.omnicore.client.keybindings.OmniKeyBindings
import dev.deftu.omnicore.client.render.ImmediateScreenRenderer
import dev.deftu.omnicore.client.render.pipeline.DrawModes
import dev.deftu.omnicore.client.render.pipeline.OmniRenderPipeline
import dev.deftu.omnicore.client.render.pipeline.VertexFormats
import dev.deftu.omnicore.client.render.state.OmniManagedBlendState
import dev.deftu.omnicore.client.render.vertex.OmniBufferBuilder
import dev.deftu.omnicore.common.*
import dev.deftu.omnicore.server.OmniServerPackets
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextFormat
import net.minecraft.client.gui.screen.ingame.HandledScreen
import org.apache.logging.log4j.LogManager
import java.awt.Color

//#if FABRIC
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
//#elseif FORGE
//#if MC >= 1.16.5
//$$ import net.minecraftforge.fml.common.Mod
//#else
//$$ import net.minecraftforge.fml.common.Mod
//$$ import net.minecraftforge.fml.common.event.FMLInitializationEvent
//#endif
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.fml.common.Mod
//#endif

const val ID = "testmod"
const val VERSION = "1.0.0"

//#if FORGE-LIKE
//#if MC >= 1.16.5
//$$ @Mod(ID)
//#else
//$$ @Mod(modid = ID, version = VERSION)
//#endif
//#endif
class TestMod
//#if FABRIC
    : ModInitializer, ClientModInitializer
//#endif
{

    private val pipeline = OmniRenderPipeline.builderWithDefaultShader(
        identifier = OmniIdentifier.create(OmniCore.ID, "screen_injector"),
        vertexFormat = VertexFormats.POSITION_COLOR,
        mode = DrawModes.QUADS,
    ).apply {
        blendState = OmniManagedBlendState.ALPHA
    }.build()

    private val logger = LogManager.getLogger(TestMod::class.java)

    private val exampleKeyBinding = OmniKeyBinding.create(
        name = "Example KeyBinding",
        category = "Example Mod",
        defaultValue = OmniKeyboard.KEY_B,
        type = OmniKeyBinding.KeyBindingType.KEY
    )

    //#if FORGE && MC >= 1.16.5
    //$$ init {
    //$$     if (OmniLoader.isPhysicalClient) {
    //$$         onInitializeClient()
    //$$     }
    //$$ }
    //#elseif NEOFORGE
    //$$ constructor(modEventBus: IEventBus) {
    //$$     if (OmniLoader.isPhysicalClient) {
    //$$         onInitializeClient()
    //$$     }
    //$$ }
    //#endif

    //#if FABRIC
    override
    //#endif
    fun onInitialize() {
        //#if FABRIC && MC >= 1.16.5
        println("Hello Fabric world!")
        OmniServerPackets.createChanneledPacketReceiver(OmniIdentifier.create("testmod:base_command")) { player, buf ->
            val message = buf.readString()
            logger.info("Received message: $message")
            true
        }
        //#endif
    }

    //#if FABRIC
    override
    //#elseif FORGE && MC <= 1.12.2
    //$$ @Mod.EventHandler
    //#else
    //$$ private
    //#endif
    fun onInitializeClient(
        //#if FORGE && MC <= 1.12.2
        //$$ event: FMLInitializationEvent
        //#endif
    ) {
        //#if FORGE && MC <= 1.12.2
        //$$ if (!event.side.isClient) {
        //$$     return
        //$$ }
        //#endif

        //#if MC >= 1.16.5
        RenderDoc.init()
        //#endif

        exampleKeyBinding.register()

        logger.info("Is $ID $VERSION on the physical client? ${OmniLoader.isPhysicalClient}")
        OmniClient.registerReloadListener(TestResourceListener)

        OmniKeyBindings.on(exampleKeyBinding) { type, state ->
            if (type.isKey && state.isPressed) {
                if (OmniClientPlayer.hasPlayer) {
                    OmniChat.displayClientMessage("Example KeyBinding pressed! (keycode: ${exampleKeyBinding.boundValue}) [should be: ${OmniKeyboard.KEY_B}]")
                }

                logger.info("Example KeyBinding pressed! (keycode: ${exampleKeyBinding.boundValue})")
            }
        }

        OmniClientCommands.command("testmod") {
            does {
                val testError = IllegalStateException("This command requires a subcommand!", IllegalStateException("This command requires a subcommand (2)!"))

                source.displayError(testError)
                OmniChat.displayClientMessage("---")
                OmniChat.displayErrorMessage(testError)
                OmniChat.displayClientMessage("---")
                OmniChat.displayErrorMessage(MCSimpleTextHolder("This is a test error message!").withFormatting(MCTextFormat.DARK_PURPLE), testError)

                OmniClientSound.play(OmniSounds.ITEM_BREAK, 1f, 1f)

                OmniChat.displayClientMessage("TestMod base command executed!")
                OmniClientPackets.send(OmniIdentifier.create("testmod:base_command"), block = {
                    OmniPackets.writeString(this, "Hello, world!")
                })

                1
            }

            command("subcommand") {
                argument("name", StringArgumentType.greedyString()) {
                    does {
                        val name = StringArgumentType.getString(this, "name")
                        source.displayError("TestMod subcommand executed with name: $name")
                        1
                    }
                }
            }

            command("screen") {
                does {
                    OmniScreen.openAfter(1, TestScreen())
                    1
                }
            }

            command("world") {
                does {
                    val world = OmniClient.currentWorld
                    val playerChunk = OmniClientPlayer.currentChunk
                    val playerBiome = OmniClientPlayer.currentBiome

                    source.displayMessage("""
                        Is day? ${world?.isDay}
                        Is night? ${world?.isNight}
                        Is raining? ${world?.isRaining}
                        Is thundering? ${world?.isThundering}
                        Is clear weather? ${world?.isClearWeather}
                        
                        Dimension: ${playerChunk?.dimension}
                        
                        Biome name: ${playerBiome?.name}
                        Translated biome name: ${playerBiome?.translatedName}
                        Biome identifier: ${playerBiome?.identifier}
                        Biome water color: ${playerBiome?.waterColor}
                    """.trimIndent())
                    1
                }
            }
        }.register()

        val topLeftColor = Color(0xFF0000) // Red
        val topRightColor = Color(0x00FF00) // Green
        val bottomLeftColor = Color(0x0000FF) // Blue
        val bottomRightColor = Color(0xFFFF00) // Yellow

        val renderX1 = 50.0
        val renderY1 = 50.0
        val renderX2 = 150.0
        val renderY2 = 150.0

        OmniCore.eventBus.on<ScreenEvent.Render.Post> {
            if (!OmniScreen.isContainerScreen(screen)) {
                return@on
            }

            matrixStack.push()
            logger.warn("Rendering on top of screen ${screen.javaClass.simpleName}")
            ImmediateScreenRenderer.render(this) { stack ->
                val buffer = OmniBufferBuilder.create(DrawModes.QUADS, VertexFormats.POSITION_COLOR)
                buffer
                    .vertex(stack, renderX1, renderY1, 0.0)
                    .color(topLeftColor)
                    .next()
                buffer
                    .vertex(stack, renderX2, renderY1, 0.0)
                    .color(topRightColor)
                    .next()
                buffer
                    .vertex(stack, renderX2, renderY2, 0.0)
                    .color(bottomRightColor)
                    .next()
                buffer
                    .vertex(stack, renderX1, renderY2, 0.0)
                    .color(bottomLeftColor)
                    .next()
                buffer.build()?.drawWithCleanup(pipeline)
            }
            matrixStack.pop()
            OmniClient.getInstance().framebuffer.blitToScreen()
        }

        //#if FABRIC && MC >= 1.16.5
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.JOIN.register { handler, sender, client ->
            println("Joined server!")
            OmniClientPackets.send(OmniIdentifier.create("testmod:base_command")) {
                writeString(OmniIdentifier.create("testmod:base_command").toString())
                writeString("Hello, world!")
            }
        }
        //#endif
    }

}
