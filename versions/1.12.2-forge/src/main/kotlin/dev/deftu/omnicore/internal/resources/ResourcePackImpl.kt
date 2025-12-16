package dev.deftu.omnicore.internal.resources

import dev.deftu.omnicore.api.resources.ResourcePack
import net.minecraft.client.resources.IResourcePack
import net.minecraft.util.ResourceLocation
import java.io.File
import java.io.InputStream
import java.util.zip.ZipFile
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.relativeTo
import kotlin.io.path.walk

public class ResourcePackImpl(private val pack: IResourcePack) : ResourcePack {
    private val packFile: File? by lazy { tryExtractPackFile(pack) }

    override fun get(
        type: ResourcePack.Type,
        location: ResourceLocation
    ): InputStream? {
        assetsOnly(type)

        return try {
            pack.getInputStream(location)
        } catch (e: Exception) {
            null
        }
    }

    override fun root(vararg pathSegments: String): InputStream? {
        return try {
            pack.getInputStream(ResourceLocation("", pathSegments.joinToString("/")))
        } catch (e: Exception) {
            null
        }
    }

    override fun namespaces(type: ResourcePack.Type): Set<String> {
        assetsOnly(type)
        return pack.resourceDomains
    }

    override fun listResources(
        type: ResourcePack.Type,
        namespace: String,
        path: String,
        filter: ResourcePack.ResourceOutput
    ) {
        assetsOnly(type)

        val file = packFile ?: return
        val normalizedNamespace = namespace.trim()
        val normalizedPath = path.trim('/')

        val locations: List<ResourceLocation> = when {
            file.isDirectory -> listFromDirectory(file, normalizedNamespace, normalizedPath)
            file.isFile && file.name.endsWith(".zip", ignoreCase = true) -> listFromZip(file, normalizedNamespace, normalizedPath)
            else -> emptyList()
        }

        for (location in locations) {
            filter.accept(location)
        }
    }

    private fun assetsOnly(type: ResourcePack.Type) {
        require(type == ResourcePack.Type.ASSETS) {
            "Only ASSETS resource type is supported in this implementation."
        }
    }

    private fun listFromDirectory(
        packRoot: File,
        namespace: String,
        path: String,
    ): List<ResourceLocation> {
        val nsRoot = packRoot.toPath().resolve("assets").resolve(namespace)
        val base = nsRoot.resolve(path)
        if (!base.exists() || !base.isDirectory()) {
            return emptyList()
        }

        val out = mutableListOf<ResourceLocation>()
        base.walk().forEach { p ->
            if (!p.isRegularFile()) {
                return@forEach
            }

            val rel = p.relativeTo(nsRoot).toString().replace('\\', '/')
            out += ResourceLocation(namespace, rel)
        }

        return out
    }

    private fun listFromZip(
        zipFile: File,
        namespace: String,
        path: String,
    ): List<ResourceLocation> {
        val prefix = "assets/$namespace/${path.trimEnd('/')}/"
        val nsPrefix = "assets/$namespace/"

        val out = mutableListOf<ResourceLocation>()
        ZipFile(zipFile).use { zip ->
            val entries = zip.entries()
            while (entries.hasMoreElements()) {
                val entry = entries.nextElement()
                if (entry.isDirectory) {
                    continue
                }

                val name = entry.name.replace('\\', '/')
                if (!name.startsWith(prefix)) {
                    continue
                }

                val rel = name.removePrefix(nsPrefix)
                out += ResourceLocation(namespace, rel)
            }
        }

        return out
    }

    private fun tryExtractPackFile(pack: IResourcePack): File? {
        val candidates = arrayOf("resourcePackFile", "field_110597_b", "a", "file")

        var clz: Class<*>? = pack.javaClass
        while (clz != null && clz != Any::class.java) {
            for (name in candidates) {
                val field = runCatching { clz.getDeclaredField(name) }.getOrNull() ?: continue
                field.isAccessible = true
                val value = runCatching { field.get(pack) }.getOrNull()
                if (value is File) {
                    return value
                }
            }

            clz = clz.superclass
        }

        return null
    }
}
