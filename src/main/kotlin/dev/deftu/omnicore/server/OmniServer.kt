package dev.deftu.omnicore.server

import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import net.minecraft.server.MinecraftServer

//#if FORGE && MC <= 1.12.2
//$$ import dev.deftu.omnicore.client.OmniClient
//$$ import dev.deftu.omnicore.common.OmniLoader
//$$ import net.minecraftforge.fml.server.FMLServerHandler
//#endif

public object OmniServer {

    //#if FABRIC || MC >= 1.16.5
    internal var server: MinecraftServer? = null
    //#endif

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun getInstance(): MinecraftServer? {
        //#if FABRIC || MC >= 1.16.5
        return server
        //#else
        //$$ if (OmniLoader.isPhysicalClient) {
        //$$     return OmniClient.integratedServer
        //$$ }
        //$$
        //$$ return FMLServerHandler.instance().server
        //#endif
    }

}
