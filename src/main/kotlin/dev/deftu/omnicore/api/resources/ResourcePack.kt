package dev.deftu.omnicore.api.resources

import dev.deftu.omnicore.internal.resources.ResourcePackImpl
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackResources
import java.io.InputStream

//#if MC >= 1.16.5
import net.minecraft.server.packs.PackType
//#endif

public interface ResourcePack {
    public companion object {
        @JvmStatic
        public fun of(pack: PackResources): ResourcePack {
            return ResourcePackImpl(pack)
        }
    }

    public enum class Type(public val directory: String) {
        ASSETS("assets"),
        DATA("data"),;

        //#if MC >= 1.16.5
        internal val packType: PackType
            get() {
                return when (this) {
                    ASSETS -> PackType.CLIENT_RESOURCES
                    DATA -> PackType.SERVER_DATA
                }
            }
        //#endif
    }

    public fun interface ResourceOutput {
        public fun accept(location: ResourceLocation)
    }

    public fun get(type: Type, location: ResourceLocation): InputStream?
    public fun root(vararg pathSegments: String): InputStream?
    public fun namespaces(type: Type): Set<String>

    public fun listResources(type: Type, namespace: String, path: String, filter: ResourceOutput)

    public fun listResources(type: Type, location: ResourceLocation, filter: ResourceOutput) {
        listResources(type, location.namespace, location.path, filter)
    }
}
