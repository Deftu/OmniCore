package com.test

import com.mojang.brigadier.arguments.StringArgumentType
import dev.deftu.omnicore.client.*
import dev.deftu.omnicore.common.OmniIdentifier
import dev.deftu.omnicore.common.OmniLoader
import dev.deftu.omnicore.common.writeString
import org.apache.logging.log4j.LogManager

//#if FABRIC
import net.fabricmc.api.ClientModInitializer
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
    : ClientModInitializer
//#endif
{

    private val logger = LogManager.getLogger(TestMod::class.java)

    private val exampleKeyBinding = OmniKeyBinding(
        name = "Example KeyBinding",
        category = "Example Mod",
        defaultValue = OmniKeyboard.KEY_M,
        type = OmniKeyBinding.Type.KEY
    ).register()

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

        logger.info("Is $ID $VERSION on the physical client? ${OmniLoader.isPhysicalClient}")

        OmniClientCommands.register(
            OmniClientCommands.literal("testmod")
                .executes { ctx ->
                    ctx.source.showMessage("TestMod base command executed!")

                    1
                }
                .then(
                    OmniClientCommands.literal("subcommand")
                        .then(
                            OmniClientCommands.argument("name", StringArgumentType.greedyString())
                                .executes { ctx ->
                                    val name = StringArgumentType.getString(ctx, "name")
                                    ctx.source.showError("TestMod subcommand executed with name: $name")

                                    1
                                }
                        )
                )
        )
    }

}
