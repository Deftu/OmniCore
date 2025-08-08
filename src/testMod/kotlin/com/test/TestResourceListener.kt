package com.test

import dev.deftu.omnicore.common.OmniIdentifier
import dev.deftu.omnicore.common.resources.SimpleResourceReloadListener
import dev.deftu.omnicore.common.resources.findFirst
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object TestResourceListener : SimpleResourceReloadListener<TestData?> {

    override val reloadIdentifier: Identifier = OmniIdentifier.create("testmod", "test_resource_listener")

    override fun reload(resourceManager: ResourceManager, executor: Executor): CompletableFuture<TestData?> {
        return CompletableFuture.supplyAsync({
            resourceManager.findFirst(OmniIdentifier.create("testmod", "tests/test_data.json"))
        }, executor).thenApplyAsync({ resource ->
            if (resource == null || !resource.isPresent) {
                return@thenApplyAsync null
            }

            val resource = resource.get()
            resource.inputStream?.use { inputStream ->
                TestData.fromJson(inputStream)
            }
        }, executor)
    }

    override fun apply(data: TestData?, resourceManager: ResourceManager, executor: Executor): CompletableFuture<Void> {
        return CompletableFuture.runAsync({
            if (data == null) {
                println("TestResourceListener: No data loaded.")
            } else {
                println("TestResourceListener: Loaded data: $data")
            }
        }, executor)
    }

}
