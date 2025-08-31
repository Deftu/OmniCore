package com.test

import com.mojang.serialization.JsonOps
import dev.deftu.omnicore.api.resources.SimpleResourceReloadListener
import dev.deftu.omnicore.common.OmniIdentifier
import dev.deftu.omnicore.api.serialization.whenError
import dev.deftu.omnicore.api.resources.findFirst
import dev.deftu.omnicore.api.resources.readJsonSafe
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import kotlin.jvm.optionals.getOrNull

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
            val json = resource.readJsonSafe() ?: return@thenApplyAsync null
            println("TestResourceListener: Loaded JSON: $json")
            TestCodecs.TEST_CODEC.parse(JsonOps.INSTANCE, json)?.whenError {
                println("TestResourceListener: Failed to parse test codec: ${it.message()}")
            }?.result()?.getOrNull()
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
