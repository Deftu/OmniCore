package dev.deftu.omnicore.common

import dev.deftu.omnicore.OmniCore
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.IntendedLoader
import dev.deftu.omnicore.annotations.Side
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import kotlin.jvm.optionals.getOrNull

//#if FABRIC
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import net.fabricmc.api.EnvType
//#elseif FORGE
//#if MC >= 1.15.2
//$$ import net.minecraftforge.fml.ModList
//$$ import net.minecraftforge.fml.ModLoadingContext
//$$ import net.minecraftforge.fml.ModContainer
//$$ import net.minecraftforge.fml.loading.FMLEnvironment
//$$ import net.minecraftforge.fml.loading.FMLLoader
//$$ import net.minecraftforge.fml.loading.FMLPaths
//$$ import net.minecraftforge.api.distmarker.Dist
//#else
//$$ import net.minecraftforge.fml.common.Loader
//$$ import net.minecraftforge.fml.common.ModContainer
//$$ import net.minecraftforge.fml.common.FMLCommonHandler
//$$ import net.minecraftforge.fml.relauncher.Side as ForgeSide
//$$ import net.minecraft.launchwrapper.Launch
//#endif
//#else
//$$ import net.neoforged.fml.ModList
//$$ import net.neoforged.fml.ModLoadingContext
//$$ import net.neoforged.fml.ModContainer
//$$ import net.neoforged.fml.loading.FMLEnvironment
//$$ import net.neoforged.fml.loading.FMLLoader
//$$ import net.neoforged.fml.loading.FMLPaths
//$$ import net.neoforged.api.distmarker.Dist
//#endif

//#if FORGE-LIKE && MC >= 1.15.2
//$$ import java.util.stream.Collectors
//#endif

//#if FORGE-LIKE && MC >= 1.16.5
//#if FORGE
//$$ import net.minecraftforge.eventbus.api.IEventBus
//#elseif NEOFORGE
//$$ import net.neoforged.bus.api.IEventBus
//#endif
//#endif

//#if FORGE-LIKE
//$$ import kotlin.io.path.inputStream
//#endif

@GameSide(Side.BOTH)
public object OmniLoader {

    /**
     * An enumeration of loaders which OmniCore supports, representing a loader that the current environment is running on.
     */
    @GameSide(Side.BOTH)
    public enum class LoaderType {
        FABRIC,
        FORGE,
        NEOFORGE
    }

    /**
     * An enumeration of physical sides that OmniCore supports, representing a physical side that the current environment is running on.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @GameSide(Side.BOTH)
    public enum class PhysicalSide {
        CLIENT,
        SERVER
    }

    /**
     * Represents a mod's basic information.
     */
    @GameSide(Side.BOTH)
    public data class ModInfo(
        val name: String,
        val id: String,
        val version: String,
        val file: Path?,
        @GameSide(Side.CLIENT)
        val iconPath: String?
    ) {

        public companion object {

            /**
             * A dummy [ModInfo] object that represents a non-existent mod.
             */
            @JvmField
            @GameSide(Side.BOTH)
            public val DUMMY: ModInfo = ModInfo("Dummy", "dummy", "0.0.0", null, null)

            @JvmStatic
            @Deprecated("Use field access instead", ReplaceWith("DUMMY"), level = DeprecationLevel.WARNING)
            public fun getDUMMY(): ModInfo {
                return DUMMY
            }

        }

        @GameSide(Side.BOTH)
        public val isDummy: Boolean
            get() = this === DUMMY

        @GameSide(Side.BOTH)
        public val modContainer: ModContainer? by lazy {
            findModContainer(id)
        }

        @GameSide(Side.BOTH)
        public val isTrueMod: Boolean
            get() = !isDummy && isLoaded

        @GameSide(Side.CLIENT)
        public val icon: InputStream?
            get() = iconPath?.let { getResourceStream(id, it) }

        /**
         * Checks if the mod is loaded.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @GameSide(Side.BOTH)
        public val isLoaded: Boolean
            get() = isModLoaded(id)

        /**
         * Checks if the mod is loaded and has the specified version.
         *
         * @since 0.13.0
         * @author Deftu
         */
        @GameSide(Side.BOTH)
        public val isVersionLoaded: Boolean
            get() = isModLoaded(id, version)

        override fun toString(): String {
            return "$name ($id@$version)"
        }

    }

    internal val versionRegex = "(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?".toRegex()

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ internal lateinit var modEventBus: IEventBus
    //#endif

    /**
     * Checks if the current environment is running in a development environment.
     *
     * @since 0.16.1
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
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

    /**
     * Gets the current Minecraft version in the standard which Preprocessor uses for comparing versions at compile-time.
     *
     * The version is padded to 6 digits, where the first 2 digits are the major version, the next 2 digits are the minor version, and the last 2 digits are the patch version. Any empty version parts are padded with 0.
     *
     * @since 0.17.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val paddedMinecraftVersion: Int
        get() {
            val version = OmniCore.minecraftVersion
            val match = versionRegex.find(version) ?: throw IllegalArgumentException("Invalid version format, could not match to regex: $version")
            val groups = match.groups

            val major = groups["major"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing major version: $version")
            val minor = groups["minor"]?.value?.toInt() ?: throw IllegalArgumentException("Invalid version format, missing minor version: $version")
            val patch = groups["patch"]?.value?.toInt() ?: 0

            return major * 10000 + minor * 100 + patch
        }

    @JvmStatic
    @GameSide(Side.BOTH)
    public val gameDir: Path
        get() {
            //#if FABRIC
            return FabricLoader.getInstance().gameDir
            //#else
            //#if MC >= 1.15.2
            //$$ return FMLPaths.GAMEDIR.get()
            //#else
            //$$ return Launch.minecraftHome.toPath()
            //#endif
            //#endif
        }

    @JvmStatic
    @GameSide(Side.BOTH)
    public val configDir: Path
        get() {
            //#if FABRIC
            return FabricLoader.getInstance().configDir
            //#else
            //#if MC >= 1.15.2
            //$$ return FMLPaths.CONFIGDIR.get()
            //#else
            //$$ return Loader.instance().configDir.toPath()
            //#endif
            //#endif
        }

    /**
     * Gets the loader type that the current environment is running on.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val loaderType: LoaderType
        get() {
            //#if FABRIC
            return LoaderType.FABRIC
            //#elseif FORGE
            //$$ return LoaderType.FORGE
            //#else
            //$$ return LoaderType.NEOFORGE
            //#endif
        }

    /**
     * Checks if the current environment is running on Fabric.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val isFabric: Boolean
        get() = loaderType == LoaderType.FABRIC

    /**
     * Checks if the current environment is running on Forge.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val isForge: Boolean
        get() = loaderType == LoaderType.FORGE

    /**
     * Checks if the current environment is running on NeoForge.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val isNeoForge: Boolean
        get() = loaderType == LoaderType.NEOFORGE

    @JvmStatic
    @GameSide(Side.BOTH)
    public val physicalSide: PhysicalSide
        get() {
            //#if FABRIC
            return when (FabricLoader.getInstance().environmentType) {
                EnvType.CLIENT -> PhysicalSide.CLIENT
                EnvType.SERVER -> PhysicalSide.SERVER
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

    /**
     * Checks if the current environment is running on the client side.
     *
     * @since 0.13.0
     * @see physicalSide
     * @see isPhysicalServer
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val isPhysicalClient: Boolean
        get() = physicalSide == PhysicalSide.CLIENT

    /**
     * Checks if the current environment is running on the server side.
     *
     * @since 0.13.0
     * @see physicalSide
     * @see isPhysicalClient
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val isPhysicalServer: Boolean
        get() = physicalSide == PhysicalSide.SERVER

    /**
     * Gets the most basic info of all loaded mods.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public val loadedMods: Set<ModInfo>
        get() {
            val value = mutableSetOf<ModInfo>()

            //#if FABRIC
            FabricLoader.getInstance().allMods.map(OmniLoader::createModInfo).forEach(value::add)
            //#else
            //#if MC >= 1.15.2
            //$$ ModList.get().applyForEachModContainer(::createModInfo).collect(Collectors.toSet()).forEach(value::add)
            //#else
            //$$ Loader.instance().modList.map(::createModInfo).forEach(value::add)
            //#endif
            //#endif

            return value
        }

    /**
     * Checks if a mod is currently in the active state.
     *
     * This method is only available on Forge, and will always return false on Fabric.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    @IntendedLoader(LoaderType.FORGE, LoaderType.NEOFORGE)
    public val hasActiveMod: Boolean
        get() {
            //#if FABRIC
            return false
            //#else
            //#if MC >= 1.15.2
            //$$ return ModLoadingContext.get().activeContainer.modId != "minecraft"
            //#else
            //$$ return Loader.instance().activeModContainer() != null
            //#endif
            //#endif
        }

    /**
     * Gets the most basic info of the active mod.
     *
     * This method is only available on Forge, and will always return [ModInfo.DUMMY] object on Fabric.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    @IntendedLoader(LoaderType.FORGE, LoaderType.NEOFORGE)
    public val activeMod: ModInfo
        get() {
            //#if FABRIC
            return ModInfo.DUMMY
            //#else
            //#if MC >= 1.15.2
            //$$ return createModInfo(ModLoadingContext.get().activeContainer)
            //#else
            //$$ if (hasActiveMod) {
            //$$     return createModInfo(Loader.instance().activeModContainer()!!)
            //$$ }
            //$$
            //$$ return ModInfo.DUMMY
            //#endif
            //#endif
        }

    /**
     * Checks if a mod is loaded and has the specified version.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public fun isModLoaded(id: String, version: String): Boolean {
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

    /**
     * Checks if a mod is loaded.
     *
     * @since 0.13.0
     * @author Deftu
     */
    @JvmStatic
    @GameSide(Side.BOTH)
    public fun isModLoaded(id: String): Boolean {
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
    @GameSide(Side.BOTH)
    public fun getModInfo(id: String): ModInfo? {
        val container = findModContainer(id)
        return container?.let(::createModInfo)
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun findModContainer(id: String): ModContainer? {
        //#if FABRIC
        return FabricLoader.getInstance().getModContainer(id).orElse(null)
        //#else
        //#if MC >= 1.15.2
        //$$ return ModList.get().getModContainerById(id).orElse(null)
        //#else
        //$$ return Loader.instance().getIndexedModList()[id]
        //#endif
        //#endif
    }


    @JvmStatic
    @GameSide(Side.BOTH)
    public fun getResourcePath(id: String, path: String): Path? {
        //#if FABRIC
        val container = FabricLoader.getInstance().getModContainer(id).orElse(null)
        return container?.rootPaths?.firstOrNull()?.resolve(path)
        //#else
        //#if MC >= 1.15.2
        //$$ return ModList.get().getModFileById(id).file.findResource(path)
        //#else
        //$$ val container = Loader.instance().getIndexedModList()[id]
        //$$ if (container != null) {
        //$$     val source = container.source
        //$$     if (source.isDirectory) {
        //$$         return source.toPath().resolve(path)
        //$$     } else {
        //$$         try {
        //$$             val zip = java.util.zip.ZipFile(source)
        //$$             val entry = zip.getEntry(path)
        //$$             if (entry != null) {
        //$$                 // Save to temp file or extract InputStream from zip
        //$$                 // Return null here; path doesn't map directly
        //$$                 zip.close()
        //$$                 return null // You cannot return a Path inside a zip cleanly
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

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun getResourceStream(id: String, path: String): InputStream? {
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

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getModIconPath(id: String): String? {
        //#if FABRIC
        val container = FabricLoader.getInstance().getModContainer(id).orElse(null)
        return container?.metadata?.getIconPath(0)?.getOrNull() // Get the first (smallest) icon path if available. Unfortunately it's not that easy to allow the consumer to specify which icon size to use, so we'll go with the smallest.
        //#else
        //#if MC >= 1.15.2
        //$$ val container = ModList.get().getModContainerById(id).orElse(null)
        //#if MC >= 1.17.1
        //$$ val logoFile = container?.modInfo?.logoFile
        //#else
        //$$ val modInfo = container?.modInfo
        //$$ val logoFile = if (modInfo is net.minecraftforge.fml.loading.moddiscovery.ModInfo) modInfo.logoFile else null
        //#endif
        //$$ return logoFile?.getOrNull()
        //#else
        //$$ val container = Loader.instance().getIndexedModList()[id]
        //$$ return container?.metadata?.logoFile
        //#endif
        //#endif
    }

    @JvmStatic
    @GameSide(Side.CLIENT)
    public fun getModIconStream(id: String): InputStream? {
        //#if FABRIC
        val iconPath = getModIconPath(id) ?: return null
        return getResourceStream(id, iconPath)
        //#else
        //#if MC >= 1.15.2
        //$$ val container = ModList.get().getModContainerById(id).orElse(null)
        //#if MC >= 1.17.1
        //$$ val logoFile = container?.modInfo?.logoFile
        //#else
        //$$ val modInfo = container?.modInfo
        //$$ val logoFile = if (modInfo is net.minecraftforge.fml.loading.moddiscovery.ModInfo) modInfo.logoFile else null
        //#endif
        //$$ return logoFile?.getOrNull()?.let { getResourceStream(id, it) }
        //#else
        //$$ val container = Loader.instance().getIndexedModList()[id]
        //$$ return container?.metadata?.logoFile?.let { getResourceStream(id, it) }
        //#endif
        //#endif
    }

    private fun createModInfo(container: ModContainer): ModInfo {
        //#if FABRIC
        return ModInfo(
            container.metadata.name,
            container.metadata.id,
            container.metadata.version.friendlyString,
            container.rootPaths[0],
            container.metadata.getIconPath(0).getOrNull() // Get the first (smallest) icon path if available. Unfortunately it's not that easy to allow the consumer to specify which icon size to use, so we'll go with the smallest.
        )
        //#else
        //#if MC >= 1.15.2
        //$$ val modFile = ModList.get().getModFileById(container.modId)
        //#if MC >= 1.17.1
        //$$ val logoFile = container.modInfo?.logoFile
        //#else
        //$$ val modInfo = container.modInfo
        //$$ val logoFile = if (modInfo is net.minecraftforge.fml.loading.moddiscovery.ModInfo) modInfo.logoFile else null
        //#endif
        //$$ return ModInfo(
        //$$     container.modInfo.displayName,
        //$$     container.modId,
        //$$     container.modInfo.version.toString(),
        //$$     modFile.file.filePath,
        //$$     logoFile?.getOrNull()
        //$$ )
        //#else
        //$$ return ModInfo(
        //$$     container.name,
        //$$     container.modId,
        //$$     container.version,
        //$$     container.source.toPath(),
        //$$     container.metadata.logoFile
        //$$ )
        //#endif
        //#endif
    }

}
