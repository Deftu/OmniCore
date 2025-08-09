package dev.deftu.omnicore.common.codecs

import com.mojang.serialization.DataResult
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import java.util.stream.IntStream

public object OmniDataResult {

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun <T> error(message: String, partialValue: T? = null): DataResult<T> {
        //#if MC >= 1.19.4
        return DataResult.error({ message }, partialValue)
        //#else
        //$$ return DataResult.error(message, partialValue)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun <T> error(supplier: () -> String?, partialValue: T? = null): DataResult<T> {
        //#if MC >= 1.19.4
        return DataResult.error(supplier, partialValue)
        //#else
        //$$ return DataResult.error(supplier(), partialValue)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
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
