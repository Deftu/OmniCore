package xyz.deftu.multi

//#if FABRIC==1
import net.fabricmc.loader.api.FabricLoader
import net.fabricmc.loader.api.ModContainer
import java.nio.file.Path

//#endif

object MultiLoader {
    enum class Loader {
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

    @JvmStatic fun getLoader() =
        //#if FABRIC==1
        Loader.FABRIC
        //#else
        //$$ Loader.FORGE
        //#endif

    @JvmStatic fun isFabric() = getLoader() == Loader.FABRIC
    @JvmStatic fun isForge() = getLoader() == Loader.FORGE

    @JvmStatic fun isModLoaded(id: String, version: String): Boolean {
        //#if FABRIC==1
        return FabricLoader.getInstance().isModLoaded(id) && FabricLoader.getInstance().getModContainer(id).get().metadata.version.friendlyString == version
        //#else
        //$$ return Loader.isModLoaded(id)
        //#endif
    }

    @JvmStatic fun isModLoaded(id: String): Boolean {
        //#if FABRIC==1
        return FabricLoader.getInstance().isModLoaded(id)
        //#else
        //$$ return Loader.isModLoaded(id)
        //#endif
    }

    @JvmStatic fun getLoadedMods(): Set<ModInfo> {
        val value = mutableSetOf<ModInfo>()

        //#if FABRIC==1
        FabricLoader.getInstance().allMods.map(::createModInfo).forEach(value::add)
        //#else
        //$$ Loader.instance().modList.map(::createModInfo).forEach(value::add)
        //#endif

        return value
    }

    @JvmStatic fun hasActiveMod(): Boolean {
        //#if FABRIC==1
        return false
        //#else
        //$$ return Loader.instance().activeModContainer() != null
        //#endif
    }

    @JvmStatic fun getActiveMod(): ModInfo {
        //#if FABRIC==1
        return ModInfo.DUMMY
        //#else
        //$$ return createModInfo(Loader.instance().activeModContainer())
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
        //$$ return ModInfo(
        //$$     container.name,
        //$$     container.modId,
        //$$     container.version,
        //$$     container.source.toPath()
        //$$ )
        //#endif
    }
}
