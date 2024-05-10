package dev.deftu.omnicore

//#if FABRIC
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
//#else
//#if MC >= 1.15.2
//$$ import net.minecraftforge.fml.ModList;
//$$ import net.minecraftforge.fml.ModLoadingContext;
//$$ import net.minecraftforge.fml.ModContainer;
//$$ import java.util.stream.Collectors
//#else
//$$ import net.minecraftforge.fml.common.Loader;
//$$ import net.minecraftforge.fml.common.ModContainer;
//#endif
//#endif

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.IntendedLoader
import dev.deftu.omnicore.annotations.Side
import java.nio.file.Path

@GameSide(Side.BOTH)
public object OmniLoader {

    @GameSide(Side.BOTH)
    public enum class LoaderType {
        FABRIC,
        FORGE
    }

    @GameSide(Side.BOTH)
    public data class ModInfo(
        val name: String,
        val id: String,
        val version: String,
        val file: Path?
    ) {
        @GameSide(Side.BOTH)
        public companion object {

            @JvmStatic
            @GameSide(Side.BOTH)
            public val DUMMY: ModInfo = ModInfo("Dummy", "dummy", "0.0.0", null)

        }

        @GameSide(Side.BOTH)
        public fun isLoaded(): Boolean = isModLoaded(id)

        @GameSide(Side.BOTH)
        public fun isVersionLoaded(): Boolean = isModLoaded(id, version)

    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun getLoaderType(): LoaderType =
        //#if FABRIC
        LoaderType.FABRIC
        //#else
        //$$ LoaderType.FORGE
        //#endif

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun isFabric(): Boolean = getLoaderType() == LoaderType.FABRIC

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun isForge(): Boolean = getLoaderType() == LoaderType.FORGE

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
    public fun getLoadedMods(): Set<ModInfo> {
        val value = mutableSetOf<ModInfo>()

        //#if FABRIC
        FabricLoader.getInstance().allMods.map(::createModInfo).forEach(value::add)
        //#else
        //#if MC >= 1.15.2
        //$$ ModList.get().applyForEachModContainer(::createModInfo).collect(Collectors.toSet()).forEach(value::add)
        //#else
        //$$ Loader.instance().modList.map(::createModInfo).forEach(value::add)
        //#endif
        //#endif

        return value
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    @IntendedLoader(LoaderType.FORGE)
    public fun hasActiveMod(): Boolean {
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

    @JvmStatic
    @GameSide(Side.BOTH)
    @IntendedLoader(LoaderType.FORGE)
    public fun getActiveMod(): ModInfo {
        //#if FABRIC
        return ModInfo.DUMMY
        //#else
        //#if MC >= 1.15.2
        //$$ return createModInfo(ModLoadingContext.get().activeContainer)
        //#else
        //$$ if (hasActiveMod()) {
        //$$     return createModInfo(Loader.instance().activeModContainer()!!)
        //$$ }
        //$$
        //$$ return ModInfo.DUMMY
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
