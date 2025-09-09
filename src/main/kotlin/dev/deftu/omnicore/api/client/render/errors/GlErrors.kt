@file:JvmName("GlErrors")

package dev.deftu.omnicore.api.client.render.errors

import java.util.function.Consumer

@JvmName("checked")
public inline fun <T> glChecked(block: () -> T): T {
    val result = block()
    val error = GlError.drain()
    if (error.isPresent) {
        throw ForwardedOpenGlException(error)
    }

    return result
}

@JvmName("checked")
public fun glChecked(runnable: Runnable) {
    glChecked {
        runnable.run()
    }
}

@JvmName("trying")
public inline fun <T> glTry(
    onError: (GlError) -> Unit?,
    block: () -> T
): T? {
    val result = block()
    val error = GlError.drain()
    if (error.isPresent) {
        onError(error)
        return null
    }

    return result
}

@JvmOverloads
@JvmName("trying")
public fun glTry(
    onError: Consumer<GlError> = Consumer {},
    runnable: Runnable
) {
    glTry(onError) {
        runnable.run()
    }
}
