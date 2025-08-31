package dev.deftu.omnicore.api.resources

import net.minecraft.resource.ResourceManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

//#if MC <= 1.21.1
//$$ import net.minecraft.util.profiler.Profiler
//#endif

//#if MC >= 1.16.5
import net.minecraft.resource.ResourceReloader
//#endif

public interface SimpleResourceReloadListener<T> : ResourceReloadListener {
    //#if MC <= 1.12.2
    //$$ private data object DirectExecutor : Executor {
    //$$     override fun execute(command: Runnable) {
    //$$         command.run()
    //$$     }
    //$$ }
    //#endif

    //#if MC >= 1.16.5
    override fun reload(
        synchronizer: ResourceReloader.Synchronizer,
        resourceManager: ResourceManager,
        //#if MC <= 1.21.1
        //$$ loadProfiler: Profiler,
        //$$ applyProfiler: Profiler,
        //#endif
        loadHandler: Executor,
        applyHandler: Executor
    ): CompletableFuture<Void> {
        return reloadInternal(synchronizer, resourceManager, loadHandler, applyHandler)
    }

    public fun reloadInternal(
        synchronizer: ResourceReloader.Synchronizer,
        resourceManager: ResourceManager,
        loadHandler: Executor,
        applyHandler: Executor
    ): CompletableFuture<Void> {
        return reload(resourceManager, loadHandler)
            .thenCompose(synchronizer::whenPrepared)
            .thenCompose { data -> apply(data, resourceManager, applyHandler) }
    }
    //#else
    //$$ override fun onResourceManagerReload(
    //$$     resourceManager: IResourceManager,
    //$$ ) {
    //$$     // This will block until we're finished reloading as the legacy
    //$$     // resource loading pipeline is synchronous.
    //$$     reload(resourceManager, DirectExecutor)
    //$$         .thenCompose { data -> apply(data, resourceManager, DirectExecutor) }
    //$$         .join()
    //$$ }
    //#endif

    public fun reload(resourceManager: ResourceManager, executor: Executor): CompletableFuture<T>

    public fun apply(data: T, resourceManager: ResourceManager, executor: Executor): CompletableFuture<Void>
}
