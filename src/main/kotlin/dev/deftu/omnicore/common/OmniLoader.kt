package dev.deftu.omnicore.common

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.IntendedLoader
import dev.deftu.omnicore.annotations.Side
import java.nio.file.Path

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
//$$ import net.minecraftforge.api.distmarker.Dist
//#else
//$$ import net.minecraftforge.fml.common.Loader
//$$ import net.minecraftforge.fml.common.ModContainer
//$$ import net.minecraftforge.fml.common.FMLCommonHandler
//$$ import net.minecraftforge.fml.relauncher.Side as ForgeSide
//#endif
//#else
//$$ import net.neoforged.fml.ModList
//$$ import net.neoforged.fml.ModLoadingContext
//$$ import net.neoforged.fml.ModContainer
//$$ import net.neoforged.fml.loading.FMLEnvironment
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
        val file: Path?
    ) {
        public companion object {

            /**
             * A dummy [ModInfo] object that represents a non-existent mod.
             */
            @JvmStatic
            @GameSide(Side.BOTH)
            public val DUMMY: ModInfo = ModInfo("Dummy", "dummy", "0.0.0", null)

        }

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

    }

    //#if FORGE-LIKE && MC >= 1.16.5
    //$$ internal lateinit var modEventBus: IEventBus
    //#endif

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

    private fun createModInfo(container: ModContainer): ModInfo {
        //#if FABRIC
        return ModInfo(
            container.metadata.name,
            container.metadata.id,
            container.metadata.version.friendlyString,
            container.rootPaths[0]
        )
        //#else
        //#if MC >= 1.15.2
        //$$ val modFile = ModList.get().getModFileById(container.modId)
        //$$ return ModInfo(
        //$$     container.modInfo.displayName,
        //$$     container.modId,
        //$$     container.modInfo.version.toString(),
        //$$     modFile.file.filePath
        //$$ )
        //#else
        //$$ return ModInfo(
        //$$     container.name,
        //$$     container.modId,
        //$$     container.version,
        //$$     container.source.toPath()
        //$$ )
        //#endif
        //#endif
    }
}
