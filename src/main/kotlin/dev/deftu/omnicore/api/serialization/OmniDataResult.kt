package dev.deftu.omnicore.api.serialization

import com.mojang.serialization.DataResult
import java.util.function.Consumer
import java.util.stream.IntStream

public object OmniDataResult {
    @JvmStatic
    @JvmOverloads
    public fun <T> error(message: String, partialValue: T? = null): DataResult<T> {
        //#if MC >= 1.19.4
        return if (partialValue == null) {
            DataResult.error { message }
        } else {
            DataResult.error({ message }, partialValue)
        }
        //#else
        //$$ return if (partialValue == null) {
        //$$     DataResult.error(message)
        //$$ } else {
        //$$     DataResult.error(message, partialValue)
        //$$ }
        //#endif
    }

    @JvmStatic
    @JvmOverloads
    public fun <T> error(supplier: () -> String?, partialValue: T? = null): DataResult<T> {
        //#if MC >= 1.19.4
        return if (partialValue == null) {
            DataResult.error(supplier)
        } else {
            DataResult.error(supplier, partialValue)
        }
        //#else
        //$$ return if (partialValue == null) {
        //$$     DataResult.error(supplier())
        //$$ } else {
        //$$     DataResult.error(supplier(), partialValue)
        //$$ }
        //#endif
    }

    @JvmStatic
    public fun <T> ifSuccess(
        result: DataResult<T>,
        //#if MC >= 1.20.6
        consumer: Consumer<T>
        //#else
        //$$ consumer: Consumer<T>
        //#endif
    ): DataResult<T> {
        //#if MC >= 1.20.6
        return result.ifSuccess(consumer)
        //#else
        //$$ result.result().ifPresent(consumer)
        //$$ return result
        //#endif
    }

    @JvmStatic
    public fun <T> ifError(
        result: DataResult<T>,
        //#if MC >= 1.20.6
        consumer: Consumer<in DataResult.Error<T>>
        //#else
        //$$ consumer: Consumer<DataResult.PartialResult<T>>
        //#endif
    ): DataResult<T> {
        //#if MC >= 1.20.6
        return result.ifError(consumer)
        //#else
        //$$ result.error().ifPresent(consumer)
        //$$ return result
        //#endif
    }

    @JvmStatic
    public fun decodeFixedLengthArray(stream: IntStream, length: Int): DataResult<IntArray> {
        val limitedStream = stream.limit(length.toLong() + 1).toArray()
        return if (limitedStream.size != length) {
            val errorMessage = "Input is not a list of $length ints"
            if (limitedStream.size >= length) {
                error(errorMessage, limitedStream.copyOf(length))
            } else {
                error(errorMessage)
            }
        } else {
            DataResult.success(limitedStream)
        }
    }
}
