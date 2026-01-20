package com.test

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import dev.deftu.omnicore.api.OmniGameMode
import dev.deftu.omnicore.api.chat.Audiences
import dev.deftu.omnicore.api.chat.TitleInfo
import dev.deftu.omnicore.api.client.OmniDesktop
import dev.deftu.omnicore.api.client.chat.OmniClientChat
import dev.deftu.omnicore.api.client.client
import dev.deftu.omnicore.api.client.commands.OmniClientCommands
import dev.deftu.omnicore.api.client.commands.argument
import dev.deftu.omnicore.api.client.commands.command
import dev.deftu.omnicore.api.client.compat.config.ConfigScreenRegistry
import dev.deftu.omnicore.api.client.input.OmniKeys
import dev.deftu.omnicore.api.client.input.keybindings.OmniKeyBinding
import dev.deftu.omnicore.api.client.input.keybindings.OmniKeyBindings
import dev.deftu.omnicore.api.client.integratedServer
import dev.deftu.omnicore.api.client.network.OmniClientNetworking
import dev.deftu.omnicore.api.client.player
import dev.deftu.omnicore.api.client.player.playerUuid
import dev.deftu.omnicore.api.client.resources.OmniClientResources
import dev.deftu.omnicore.api.client.sound.OmniClientSound
import dev.deftu.omnicore.api.client.world
import dev.deftu.omnicore.api.color.OmniColor
import dev.deftu.omnicore.api.commands.types.ResourceLocationArgumentType
import dev.deftu.omnicore.api.commands.types.OmniDirectionalAxisArgumentType
import dev.deftu.omnicore.api.commands.types.OmniSoundArgumentType
import dev.deftu.omnicore.api.commands.types.color.OmniColorArgumentType
import dev.deftu.omnicore.api.commands.types.gamemode.GameModeArgumentType
import dev.deftu.omnicore.api.direction.OmniDirectionalAxis
import dev.deftu.omnicore.api.locationOrThrow
import dev.deftu.omnicore.api.loader.OmniLoader
import dev.deftu.omnicore.api.network.OmniNetworking
import dev.deftu.omnicore.api.player.biomeData
import dev.deftu.omnicore.api.player.chunkData
import dev.deftu.omnicore.api.resources.findFirstOrNull
import dev.deftu.omnicore.api.resources.readString
import dev.deftu.omnicore.api.sound.OmniSound
import dev.deftu.omnicore.api.sound.OmniSounds
import dev.deftu.omnicore.api.world.isClearWeather
import dev.deftu.omnicore.api.world.isDayTime
import dev.deftu.textile.Text
import dev.deftu.textile.minecraft.MCTextStyle
import dev.deftu.textile.minecraft.TextColors
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
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
        category = locationOrThrow(ID, "example_category"),
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
        println("Hello common world!")
        OmniNetworking.register(TestPacketPayload.TYPE) { payload ->
            logger.info("Received test packet on the server with message: ${payload.message}")

            // Echo the packet back to the client
            if (player is ServerPlayer) {
                OmniNetworking.send(player as ServerPlayer, payload)
            }
        }
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
        //$$ onInitialize() // Common init
        //$$ if (!event.side.isClient) {
        //$$     return
        //$$ }
        //#endif

        //#if MC >= 1.19.2
        RenderDoc.init()
        //#endif

        println(OmniDesktop)
        exampleKeyBinding.register()

        ConfigScreenRegistry.register(ID) { _ ->
            TestScreen()
        }

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

        OmniClientCommands.command("testmod") {
            alias("tm", "testing")

            runs {
                val testError = IllegalStateException("This command requires a subcommand!", IllegalStateException("This command requires a subcommand (2)!"))

                OmniClientChat.displayErrorMessage(testError)
                OmniClientChat.displayChatMessage("---")
                OmniClientChat.displayErrorMessage(testError)
                OmniClientChat.displayChatMessage("---")
                OmniClientChat.displayErrorMessage(Text.literal("This is a test error message!").setStyle(MCTextStyle(color = TextColors.DARK_PURPLE).build()), testError)

                OmniClientSound.play(OmniSounds.ENTITY.itemBreak, 1f, 1f)

                OmniClientChat.displayChatMessage("TestMod base command executed!")

                1
            }

            then("subcommand") {
                argument("name", StringArgumentType.greedyString()) {
                    runs { ctx ->
                        val name = ctx.argument<String>("name")

                        Audiences.CLIENT.sendChat("Hello, $name!")

                        val server = integratedServer!!
                        Audiences.of(
                            Audiences.CLIENT,
                            Audiences.CONSOLE,
                            Audiences.broadcasting(server),
                            Audiences.EMPTY,
                            Audiences.single(server.playerList.players.first())
                        ).let { audience ->
                            // Delegates actions all of the provided audiences
                            audience.sendChat("Hello again, $name!")
                            audience.forEach { println("Sent message to audience: $it") }
                            audience.sendActionBar("Action bar message from $name!")
                            audience.sendTitle(TitleInfo.of("Title to $name", "Subtitle to $name"))
                            audience.playSound(OmniSounds.ENTITY.levelUp, 1f, 1f)
                        }

                        ctx.source.replyChat("TestMod subcommand executed with name: $name")
                    }
                }
            }

            then("title") {
                runs { ctx ->
                    ctx.source.replyTitle(TitleInfo.of("You smell of soot and poo!", "Go take a shower..."))
                }
            }

            then("screen") {
                requires { src -> src.world != null }

                runs { ctx ->
                    ctx.source.openScreen(TestScreen())
                }

                argument("create", BoolArgumentType.bool()) {
                    requires { src -> src.world != null }

                    runs { ctx ->
                        val create = ctx.argument<Boolean>("create")
                        ctx.source.openScreen(TestScreen(createsTexture = create))
                    }
                }
            }

            then("world") {
                runs { ctx ->
                    val world = world
                    val playerChunk = player?.chunkData
                    val playerBiome = player?.biomeData

                    ctx.source.replyChat("""
                        Is day? ${world?.isDayTime}
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

            then("packets") {
                runs { ctx ->
                    sendTestPacket()
                    ctx.source.replyChat("Sent test packet to the server!")
                }

                argument("message", StringArgumentType.greedyString()) {
                    runs { ctx ->
                        val message = ctx.argument<String>("message")
                        sendTestPacket(message)
                        ctx.source.replyChat("Sent test packet to the server with custom message: $message")
                    }
                }
            }

            then("resource") {
                argument("location", ResourceLocationArgumentType.create()) {
                    runs { ctx ->
                        val location = ctx.argument<ResourceLocation>("location")
                        val resource = client.resourceManager.findFirstOrNull(location)
                        if (resource != null) {
                            println("""
                                Resource contents for $location:
                                --------------------------------
                                ${resource.readString()}
                            """.trimIndent())

                            ctx.source.replyChat("Found resource: $location")
                        } else {
                            ctx.source.replyChat("Resource not found: $location")
                        }
                    }
                }
            }

            then("ogm") { // OmniGameMode!
                argument("mode", GameModeArgumentType.create()) {
                    runs { ctx ->
                        val mode = ctx.argument<OmniGameMode>("mode")
                        integratedServer?.playerList?.getPlayer(playerUuid)?.setGameMode(mode.vanilla)
                        ctx.source.replyChat("Selected game mode: $mode")
                    }
                }
            }

            val allSounds = listOf(
                OmniSounds.BLOCK.anvilUse, OmniSounds.BLOCK.anvilBreak, OmniSounds.BLOCK.anvilFall, OmniSounds.BLOCK.anvilLand, OmniSounds.BLOCK.anvilPlace,
                OmniSounds.BLOCK.doorOpen, OmniSounds.BLOCK.doorClose,
                OmniSounds.BLOCK.chestOpen, OmniSounds.BLOCK.chestClose,

                OmniSounds.ENTITY.experienceOrb, OmniSounds.ENTITY.levelUp,
                OmniSounds.ENTITY.itemPickUp, OmniSounds.ENTITY.itemBreak,

                OmniSounds.NOTE_BLOCK.basedrum, OmniSounds.NOTE_BLOCK.bass, OmniSounds.NOTE_BLOCK.harp, OmniSounds.NOTE_BLOCK.hat, OmniSounds.NOTE_BLOCK.pling, OmniSounds.NOTE_BLOCK.snare,
                OmniSounds.NOTE_BLOCK.bell, OmniSounds.NOTE_BLOCK.chime, OmniSounds.NOTE_BLOCK.flute, OmniSounds.NOTE_BLOCK.guitar, OmniSounds.NOTE_BLOCK.xylophone,
                OmniSounds.NOTE_BLOCK.ironXylophone, OmniSounds.NOTE_BLOCK.cowBell, OmniSounds.NOTE_BLOCK.didgeridoo, OmniSounds.NOTE_BLOCK.bit, OmniSounds.NOTE_BLOCK.banjo,

                OmniSounds.PLAYER.hurt, OmniSounds.PLAYER.death, OmniSounds.PLAYER.swim, OmniSounds.PLAYER.splash, OmniSounds.PLAYER.burp, OmniSounds.PLAYER.fallBig, OmniSounds.PLAYER.fallSmall,

                OmniSounds.WOLF.shake, OmniSounds.WOLF.step, OmniSounds.WOLF.bark, OmniSounds.WOLF.death, OmniSounds.WOLF.growl, OmniSounds.WOLF.hurt, OmniSounds.WOLF.panting, OmniSounds.WOLF.whine,

                OmniSounds.MISCELLANEOUS.buttonClick
            )

            var currentSoundIndex = 0
            then("sounds") {
                runs { ctx ->
                    val sound = allSounds[currentSoundIndex++]
                    if (currentSoundIndex >= allSounds.size) {
                        currentSoundIndex = 0
                    }

                    OmniClientSound.play(sound, 1f, 1f)
                    ctx.source.replyChat(Text.literal("Played sound ")
                        .append(Text.literal((currentSoundIndex - 1).toString()).setStyle(MCTextStyle.color(TextColors.GREEN)))
                        .append(Text.literal(" of ${allSounds.size - 1} (${sound.location})"))
                    )
                }

                then("exact") {
                    argument("sound", OmniSoundArgumentType.many(allSounds)) {
                        runs { ctx ->
                            val sound = ctx.argument<OmniSound>("sound")
                            OmniClientSound.play(sound, 1f, 1f)
                            ctx.source.replyChat("Played exact sound: ${sound.location}")
                        }
                    }
                }
            }

            then("color") {
                argument("value", OmniColorArgumentType.create()) {
                    runs { ctx ->
                        val color = ctx.argument<OmniColor>("value")
                        ctx.source.replyChat(Text.literal("You entered the color: ")
                            .append(Text.literal(color.toHexRGB()).setStyle(MCTextStyle.color(TextColors.hex(color.toHexRGB()))))
                            .append(Text.literal("  | $color"))
                        )
                    }
                }
            }

            then("axis") {
                argument("value", OmniDirectionalAxisArgumentType.create()) {
                    runs { ctx ->
                        val axis = ctx.argument<OmniDirectionalAxis>("value")
                        ctx.source.replyChat("You entered the directional axis: $axis")
                    }
                }
            }
        }.register()
    }

    private fun sendTestPacket(message: String = "Hello from the client!") {
        OmniClientNetworking.send(TestPacketPayload(message))
    }
}
