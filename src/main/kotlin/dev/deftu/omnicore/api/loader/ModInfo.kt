package dev.deftu.omnicore.api.loader

import java.nio.file.Path
import java.util.Optional

//#if FABRIC
import net.fabricmc.loader.api.ModContainer
//#elseif FORGE
//#if MC >= 1.16.5
//$$ import net.minecraftforge.fml.ModList
//$$ import net.minecraftforge.fml.ModContainer
//#else
//$$ import net.minecraftforge.fml.common.ModContainer
//#endif
//#else
//$$ import net.neoforged.fml.ModList
//$$ import net.neoforged.fml.ModContainer
//#endif

public data class ModInfo(
    val id: String,
    val name: String,
    val version: String,
    val file: Path?,
    var container: Optional<ModContainer> = OmniLoader.findContainer(id),
) {
    public companion object {
        @JvmField
        public val DUMMY: ModInfo = ModInfo("dummy", "Dummy Mod", "0.0.0", null)

        @JvmStatic
        public fun wrap(mod: ModContainer): ModInfo {
            //#if FABRIC
            return ModInfo(
                mod.metadata.name,
                mod.metadata.id,
                mod.metadata.version.friendlyString,
                mod.rootPaths[0],
                Optional.of(mod),
            )
            //#else
            //#if MC >= 1.15.2
            //$$ val modFile = ModList.get().getModFileById(mod.modId)
            //$$ return ModInfo(
            //$$     mod.modInfo.displayName,
            //$$     mod.modId,
            //$$     mod.modInfo.version.toString(),
            //$$     modFile.file.filePath,
            //$$     Optional.of(mod),
            //$$ )
            //#else
            //$$ return ModInfo(
            //$$     mod.name,
            //$$     mod.modId,
            //$$     mod.version,
            //$$     mod.source.toPath(),
            //$$     Optional.of(mod),
            //$$ )
            //#endif
            //#endif
        }
    }

    public val isLoaded: Boolean
        get() = OmniLoader.isLoaded(id)

    public val isVersionMatch: Boolean
        get() = OmniLoader.isLoaded(id, version)

    override fun toString(): String {
        return "$name ($id@$version)"
    }
}
