package dev.deftu.omnicore.api.resources

import net.minecraft.server.packs.resources.ResourceManager
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

//#if MC <= 1.21.1
//$$ import net.minecraft.util.profiling.ProfilerFiller
//#endif

//#if MC >= 1.16.5
import net.minecraft.server.packs.resources.PreparableReloadListener
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
    //#if MC >= 1.21.9
    override fun reload(
        reloadState: PreparableReloadListener.SharedState,
        loadHandler: Executor,
        synchronizer: PreparableReloadListener.PreparationBarrier,
        applyExecutor: Executor
    ): CompletableFuture<Void> {
        return reloadInternal(synchronizer, reloadState.resourceManager(), loadHandler, applyExecutor)
    }
    //#else
    //$$ override fun reload(
    //$$     synchronizer: PreparableReloadListener.PreparationBarrier,
    //$$     resourceManager: ResourceManager,
    //#if MC <= 1.21.1
    //$$ loadProfiler: ProfilerFiller,
    //$$ applyProfiler: ProfilerFiller,
    //#endif
    //$$     loadHandler: Executor,
    //$$     applyHandler: Executor
    //$$ ): CompletableFuture<Void> {
    //$$     return reloadInternal(synchronizer, resourceManager, loadHandler, applyHandler)
    //$$ }
    //#endif

    public fun reloadInternal(
        synchronizer: PreparableReloadListener.PreparationBarrier,
        resourceManager: ResourceManager,
        loadHandler: Executor,
        applyHandler: Executor
    ): CompletableFuture<Void> {
        return reload(resourceManager, loadHandler)
            .thenCompose(synchronizer::wait)
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
