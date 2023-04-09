package xyz.deftu.multi

//#if FABRIC==1
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
//#else
//#if MC>=11502
//$$ import net.minecraftforge.fml.ModList;
//$$ import net.minecraftforge.fml.ModLoadingContext;
//$$ import net.minecraftforge.fml.ModContainer;
//$$ import java.util.stream.Collectors
//#else
//$$ import net.minecraftforge.fml.common.Loader;
//$$ import net.minecraftforge.fml.common.ModContainer;
//#endif
//#endif

import java.nio.file.Path

object MultiLoader {
    enum class LoaderType {
        FABRIC,
        FORGE
    }

    data class ModInfo(
        val name: String,
        val id: String,
        val version: String,
        val file: Path?
    ) {
        companion object {
            @JvmStatic
            val DUMMY = ModInfo("Dummy", "dummy", "0.0.0", null)
        }

        fun isLoaded() = isModLoaded(id)
        fun isVersionLoaded() = isModLoaded(id, version)
    }

    @JvmStatic fun getLoaderType() =
        //#if FABRIC==1
        LoaderType.FABRIC
        //#else
        //$$ LoaderType.FORGE
        //#endif

    @JvmStatic fun isFabric() = getLoaderType() == LoaderType.FABRIC
    @JvmStatic fun isForge() = getLoaderType() == LoaderType.FORGE

    @JvmStatic fun isModLoaded(id: String, version: String): Boolean {
        //#if FABRIC==1
        return FabricLoader.getInstance().isModLoaded(id) && FabricLoader.getInstance().getModContainer(id).get().metadata.version.friendlyString == version
        //#else
        //#if MC>=11502
        //$$ return ModList.get().isLoaded(id) && ModList.get().getModContainerById(id).get().getModInfo().getVersion().toString() == version
        //#else
        //$$ return Loader.isModLoaded(id) && Loader.instance().getIndexedModList()[id]?.version == version
        //#endif
        //#endif
    }

    @JvmStatic fun isModLoaded(id: String): Boolean {
        //#if FABRIC==1
        return FabricLoader.getInstance().isModLoaded(id)
        //#else
        //#if MC>=11502
        //$$ return ModList.get().isLoaded(id)
        //#else
        //$$ return Loader.isModLoaded(id)
        //#endif
        //#endif
    }

    @JvmStatic fun getLoadedMods(): Set<ModInfo> {
        val value = mutableSetOf<ModInfo>()

        //#if FABRIC==1
        FabricLoader.getInstance().allMods.map(::createModInfo).forEach(value::add)
        //#else
        //#if MC>=11502
        //$$ ModList.get().applyForEachModContainer(::createModInfo).collect(Collectors.toSet()).forEach(value::add)
        //#else
        //$$ Loader.instance().modList.map(::createModInfo).forEach(value::add)
        //#endif
        //#endif

        return value
    }

    @JvmStatic fun hasActiveMod(): Boolean {
        //#if FABRIC==1
        return false
        //#else
        //#if MC>=11502
        //$$ return ModLoadingContext.get().activeContainer.modId != "minecraft"
        //#else
        //$$ return Loader.instance().activeModContainer() != null
        //#endif
        //#endif
    }

    @JvmStatic fun getActiveMod(): ModInfo {
        //#if FABRIC==1
        return ModInfo.DUMMY
        //#else
        //#if MC>=11502
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
        //#if FABRIC==1
        return ModInfo(
            container.metadata.name,
            container.metadata.id,
            container.metadata.version.friendlyString,
            container.rootPaths[0]
        )
        //#else
        //#if MC>=11502
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
