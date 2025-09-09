@file:JvmName("OmniPaths")

package dev.deftu.omnicore.api

import java.nio.file.Path

//#if FABRIC
import net.fabricmc.loader.api.FabricLoader
//#else
//#if MC >= 1.16.5
//#if FORGE
//$$ import net.minecraftforge.fml.loading.FMLPaths
//#else
//$$ import net.neoforged.fml.loading.FMLPaths
//#endif
//#else
//$$ import net.minecraft.launchwrapper.Launch
//$$ import net.minecraftforge.fml.common.Loader
//#endif
//#endif

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
