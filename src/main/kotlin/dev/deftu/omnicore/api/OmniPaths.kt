@file:JvmName("OmniPaths")

package dev.deftu.omnicore.api

import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

public val gameDirectory: Path
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

public val configDirectory: Path
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
