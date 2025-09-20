package dev.deftu.omnicore.internal

//#if FORGE-LIKE
//#if FORGE
//#if MC >= 1.16.5
//$$ import net.minecraftforge.eventbus.api.IEventBus
//#endif
//$$ import net.minecraftforge.common.MinecraftForge
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//$$ import net.neoforged.neoforge.common.NeoForge
//#endif
//$$
//$$ internal typealias Forge =
//#if FORGE
//$$     MinecraftForge
//#elseif NEOFORGE
//$$     NeoForge
//#endif
//$$
//$$ internal inline val forgeEventBus get() = Forge.EVENT_BUS
//#if MC >= 1.16.5
//$$ internal lateinit var modEventBus: IEventBus
//#endif
//#endif
