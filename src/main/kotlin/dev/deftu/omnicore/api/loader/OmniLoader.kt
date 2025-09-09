package dev.deftu.omnicore.api.loader

import dev.deftu.omnicore.api.Side
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import java.io.InputStream
import java.nio.file.Files
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

public object OmniLoader {
    @JvmStatic
    public val loader: ModLoader
        get() {
            //#if FABRIC
            return ModLoader.FABRIC
            //#elseif FORGE
            //$$ return ModLoader.FORGE
            //#else
            //$$ return ModLoader.NEOFORGE
            //#endif
        }

    @JvmStatic
    public val isFabric: Boolean
        get() = loader == ModLoader.FABRIC

    @JvmStatic
    public val isForge: Boolean
        get() = loader == ModLoader.FORGE

    @JvmStatic
    public val isNeoForge: Boolean
        get() = loader == ModLoader.NEOFORGE

    @JvmStatic
    public val physicalSide: Side
        get() {
            //#if FABRIC
            return when (FabricLoader.getInstance().environmentType) {
                EnvType.CLIENT -> Side.CLIENT
                EnvType.SERVER -> Side.SERVER
                else -> throw IllegalStateException("Unknown physical side")
            }
            //#else
            //#if MC >= 1.15.2
            //$$ return when (FMLEnvironment.dist) {
            //$$     Dist.CLIENT -> PhysicalSide.CLIENT
            //$$     Dist.DEDICATED_SERVER -> PhysicalSide.SERVER
            //$$     else -> throw IllegalStateException("Unknown physical side")
            //$$ }
            //#else
            //$$ return when (FMLCommonHandler.instance().side) {
            //$$     ForgeSide.CLIENT -> PhysicalSide.CLIENT
            //$$     ForgeSide.SERVER -> PhysicalSide.SERVER
            //$$     else -> throw IllegalStateException("Unknown physical side")
            //$$ }
            //#endif
            //#endif
        }

    @JvmStatic
    public val isPhysicalClient: Boolean
        get() = physicalSide == Side.CLIENT

    @JvmStatic
    public val isPhysicalServer: Boolean
        get() = physicalSide == Side.SERVER

    @JvmStatic
    public val isDevelopment: Boolean
        get() {
            //#if FABRIC
            return FabricLoader.getInstance().isDevelopmentEnvironment
            //#else
            //#if MC >= 1.15.2
            //$$ return !FMLLoader.isProduction()
            //#else
            //$$ return Launch.blackboard["fml.deobfuscatedEnvironment"] as Boolean
            //#endif
            //#endif
        }

    @JvmStatic
    public val mods: Set<ModInfo>
        get() = buildSet {
            //#if FABRIC
            FabricLoader.getInstance().allMods.map(ModInfo::wrap).forEach(::add)
            //#else
            //#if MC >= 1.15.2
            //$$ ModList.get().applyForEachModContainer(ModInfo::wrap).collect(Collectors.toSet()).forEach(::add)
            //#else
            //$$ Loader.instance().modList.map(ModInfo::wrap).forEach(::add)
            //#endif
            //#endif
        }

    @JvmStatic
    public fun isLoaded(id: String, version: String): Boolean {
        //#if FABRIC
        return FabricLoader.getInstance().isModLoaded(id) && FabricLoader.getInstance().getModContainer(id).get().metadata.version.friendlyString == version
        //#else
        //#if MC >= 1.15.2
        //$$ return ModList.get().isLoaded(id) && ModList.get().getModContainerById(id).get().getModInfo().getVersion().toString() == version
        //#else
        //$$ return Loader.isModLoaded(id) && Loader.instance().getIndexedModList()[id]?.version == version
        //#endif
        //#endif
    }

    @JvmStatic
    public fun isLoaded(id: String): Boolean {
        //#if FABRIC
        return FabricLoader.getInstance().isModLoaded(id)
        //#else
        //#if MC >= 1.15.2
        //$$ return ModList.get().isLoaded(id)
        //#else
        //$$ return Loader.isModLoaded(id)
        //#endif
        //#endif
    }

    @JvmStatic
    public fun findContainer(id: String): Optional<ModContainer> {
        //#if FABRIC
        return FabricLoader.getInstance().getModContainer(id)
        //#else
        //#if MC >= 1.15.2
        //$$ return ModList.get().getModContainerById(id)
        //#else
        //$$ return Optional.ofNullable(Loader.instance().getIndexedModList()[id])
        //#endif
        //#endif
    }

    @JvmStatic
    public fun findContainerOrNull(id: String): ModContainer? {
        return findContainer(id).orElse(null)
    }

    @JvmStatic
    public fun findContainerOrThrow(id: String): ModContainer {
        return findContainer(id).orElseThrow { IllegalArgumentException("Mod with ID '$id' not found") }
    }

    @JvmStatic
    public fun findMod(id: String): Optional<ModInfo> {
        return findContainer(id).map(ModInfo::wrap)
    }

    @JvmStatic
    public fun findModOrNull(id: String): ModInfo? {
        return findContainerOrNull(id)?.let(ModInfo::wrap)
    }

    @JvmStatic
    public fun findModOrThrow(id: String): ModInfo {
        return ModInfo.wrap(findContainerOrThrow(id))
    }

    @JvmStatic
    public fun getResourceOrNull(id: String, path: String): InputStream? {
        //#if FABRIC
        val container = FabricLoader.getInstance().getModContainer(id).orElse(null)
        return container?.findPath(path)?.getOrNull()?.takeIf(Files::exists)?.let(Files::newInputStream)
        //#else
        //#if MC >= 1.15.2
        //$$ return ModList.get().getModFileById(id).file.findResource(path)?.inputStream()
        //#else
        //$$ val container = Loader.instance().getIndexedModList()[id]
        //$$ if (container != null) {
        //$$     val source = container.source
        //$$     if (source.isDirectory) {
        //$$         return source.toPath().resolve(path).toFile().inputStream()
        //$$     } else {
        //$$         try {
        //$$             val zip = java.util.zip.ZipFile(source)
        //$$             val entry = zip.getEntry(path)
        //$$             if (entry != null) {
        //$$                 return zip.getInputStream(entry)
        //$$             }
        //$$         } catch (e: Exception) {
        //$$             e.printStackTrace()
        //$$         }
        //$$     }
        //$$ }
        //$$ return null
        //#endif
        //#endif
    }
}
