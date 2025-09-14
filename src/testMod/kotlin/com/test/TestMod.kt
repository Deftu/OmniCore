package com.test

import com.mojang.brigadier.arguments.StringArgumentType
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.commands.argument
import dev.deftu.omnicore.api.client.commands.command
import dev.deftu.omnicore.api.client.input.OmniKeys
import dev.deftu.omnicore.api.client.input.keybindings.OmniKeyBinding
import dev.deftu.omnicore.api.client.input.keybindings.OmniKeyBindings
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import dev.deftu.omnicore.api.client.player
import dev.deftu.omnicore.api.client.resources.OmniClientResources
import dev.deftu.omnicore.api.client.sound.OmniClientSound
import dev.deftu.omnicore.api.client.world
import dev.deftu.omnicore.api.loader.OmniLoader
import dev.deftu.omnicore.api.network.OmniNetworking
import dev.deftu.omnicore.api.player.biomeData
import dev.deftu.omnicore.api.player.chunkData
import dev.deftu.omnicore.api.sound.OmniSounds
import dev.deftu.omnicore.api.world.isClearWeather
import dev.deftu.textile.minecraft.MCSimpleTextHolder
import dev.deftu.textile.minecraft.MCTextFormat
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.LogManager

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

    private val logger = LogManager.getLogger(TestMod::class.java)

    private val exampleKeyBinding = OmniKeyBinding.create(
        name = "Example KeyBinding",
        category = "Example Mod",
        defaultValue = OmniKeys.KEY_B,
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
        // TestItems.initialize()
        OmniNetworking.register(TestPacketPayload.TYPE) { payload ->
            logger.info("Received test packet on the server with message: ${payload.message}")

            // Echo the packet back to the client
            if (player is ServerPlayerEntity) {
                OmniNetworking.send(player as ServerPlayerEntity, payload)
            }
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

        exampleKeyBinding.register()

        logger.info("Is $ID $VERSION on the physical client? ${OmniLoader.isPhysicalClient}")
        OmniClientResources.registerReloadListener(TestResourceListener)

        OmniKeyBindings.on(exampleKeyBinding) { type, state ->
            if (type.isKey && state.isPressed) {
                if (player != null) {
                    OmniClientChat.displayChatMessage("Example KeyBinding pressed! (keycode: ${exampleKeyBinding.boundValue}) [should be: ${OmniKeys.KEY_B}]")
                }

                logger.info("Example KeyBinding pressed! (keycode: ${exampleKeyBinding.boundValue})")
            }
        }

        OmniClientNetworking.register(TestPacketPayload.TYPE) { payload ->
            logger.info("Received test packet with message: ${payload.message}")
            if (player != null) {
                OmniClientChat.displayChatMessage("Received test packet with message: ${payload.message}")
            }
        }

        command("testmod") {
            runs {
                val testError = IllegalStateException("This command requires a subcommand!", IllegalStateException("This command requires a subcommand (2)!"))

                OmniClientChat.displayErrorMessage(testError)
                OmniClientChat.displayChatMessage("---")
                OmniClientChat.displayErrorMessage(testError)
                OmniClientChat.displayChatMessage("---")
                OmniClientChat.displayErrorMessage(MCSimpleTextHolder("This is a test error message!").withFormatting(MCTextFormat.DARK_PURPLE), testError)

                OmniClientSound.play(OmniSounds.ITEM_BREAK, 1f, 1f)

                OmniClientChat.displayChatMessage("TestMod base command executed!")
                sendTestPacket()

                1
            }

            then("subcommand") {
                argument("name", StringArgumentType.greedyString()) {
                    runs { ctx ->
                        val name = ctx.argument<String>("name")
                        ctx.source.replyChat("TestMod subcommand executed with name: $name")
                    }
                }
            }

            command("screen") {
                requires { src -> src.world != null }

                runs { ctx ->
                    ctx.source.openScreen(TestScreen())
                }
            }

            command("world") {
                runs { ctx ->
                    val world = world
                    val playerChunk = player?.chunkData
                    val playerBiome = player?.biomeData

                    ctx.source.replyChat("""
                        Is day? ${world?.isDay}
                        Is raining? ${world?.isRaining}
                        Is thundering? ${world?.isThundering}
                        Is clear weather? ${world?.isClearWeather}
                        
                        Dimension: ${playerChunk?.dimension}
                        
                        Biome name: ${playerBiome?.name}
                        Biome identifier: ${playerBiome?.identifier}
                        Biome water color: ${playerBiome?.waterColor}
                    """.trimIndent())
                }
            }
        }.register()

        //#if FABRIC && MC >= 1.16.5
        net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents.JOIN.register { handler, sender, client ->
            println("Joined server!")
            sendTestPacket()
        }
        //#endif
    }

    private fun sendTestPacket() {
        OmniClientNetworking.send(TestPacketPayload("Hello from the client!"))
    }
}
