package dev.deftu.omnicore.api.serialization

import com.mojang.serialization.DataResult
import java.util.function.Consumer

public fun <T> DataResult<T>.whenSuccess(consumer: Consumer<T>): DataResult<T> {
    return OmniDataResult.ifSuccess(this, consumer)
}

public fun <T> DataResult<T>.whenError(
    //#if MC >= 1.20.6
    consumer: Consumer<in DataResult.Error<T>>
    //#else
    //$$ consumer: Consumer<DataResult.PartialResult<T>>
    //#endif
): DataResult<T> {
    return OmniDataResult.ifError(this, consumer)
}
