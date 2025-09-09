@file:Suppress("UNCHECKED_CAST")

package dev.deftu.omnicore.api.network.codec

import com.mojang.datafixers.util.Function11
import com.mojang.datafixers.util.Function3
import com.mojang.datafixers.util.Function4
import com.mojang.datafixers.util.Function5
import com.mojang.datafixers.util.Function6
import com.mojang.datafixers.util.Function7
import com.mojang.datafixers.util.Function8
import com.mojang.datafixers.util.Function9
import io.netty.buffer.ByteBuf
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.UnaryOperator

/**
 * A rough backport of Mojang's StreamCodec/PacketCodec from Minecraft 1.20.4+.
 *
 * Separate from DataFixer's codecs.
 */
public interface StreamCodec<V, B> : StreamDecoder<B, V>, StreamEncoder<B, V> {
    public companion object {
        @JvmStatic
        public fun <V, B> of(
            encoder: StreamEncoder<B, V>,
            decoder: StreamDecoder<B, V>
        ): StreamCodec<V, B> = object : StreamCodec<V, B> {
            override fun decode(input: B): V = decoder.decode(input)
            override fun encode(output: B, value: V) = encoder.encode(output, value)
        }

        @JvmStatic
        public fun <V, B> ofMember(
            encoder: StreamMemberEncoder<B, V>,
            decoder: StreamDecoder<B, V>
        ): StreamCodec<V, B> = object : StreamCodec<V, B> {
            override fun decode(input: B): V = decoder.decode(input)
            override fun encode(output: B, value: V) = encoder.encode(value, output)
        }

        @JvmStatic
        public fun <V, B> unit(expected: V): StreamCodec<V, B> = object : StreamCodec<V, B> {
            override fun decode(input: B): V = expected
            override fun encode(output: B, value: V) {
                if (value != expected) error("Can't encode '$value', expected '$expected'")
            }
        }

        @JvmStatic
        public fun <V, B> recursive(
            op: UnaryOperator<StreamCodec<V, B>>
        ): StreamCodec<V, B> = object : StreamCodec<V, B> {
            private val inner: StreamCodec<V, B> by lazy { op.apply(this) }
            override fun decode(input: B): V = inner.decode(input)
            override fun encode(output: B, value: V) = inner.encode(output, value)
        }

        @JvmStatic
        public fun <B, C, T1> composite(
            c1: StreamCodec<T1, B>,
            get1: Function<in C, out T1>,
            mk: Function<in T1, out C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(c1.decode(input))
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            mk: BiFunction<in T1, in T2, out C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(c1.decode(input), c2.decode(input))
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            mk: Function3<T1, T2, T3, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(c1.decode(input), c2.decode(input), c3.decode(input))
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            mk: Function4<T1, T2, T3, T4, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(c1.decode(input), c2.decode(input), c3.decode(input), c4.decode(input))
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4, T5> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            c5: StreamCodec<T5, B>, get5: Function<in C, out T5>,
            mk: Function5<T1, T2, T3, T4, T5, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(
                c1.decode(input), c2.decode(input), c3.decode(input), c4.decode(input), c5.decode(input)
            )
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
                c5.encode(output, get5.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4, T5, T6> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            c5: StreamCodec<T5, B>, get5: Function<in C, out T5>,
            c6: StreamCodec<T6, B>, get6: Function<in C, out T6>,
            mk: Function6<T1, T2, T3, T4, T5, T6, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(
                c1.decode(input), c2.decode(input), c3.decode(input),
                c4.decode(input), c5.decode(input), c6.decode(input)
            )
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
                c5.encode(output, get5.apply(value))
                c6.encode(output, get6.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4, T5, T6, T7> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            c5: StreamCodec<T5, B>, get5: Function<in C, out T5>,
            c6: StreamCodec<T6, B>, get6: Function<in C, out T6>,
            c7: StreamCodec<T7, B>, get7: Function<in C, out T7>,
            mk: Function7<T1, T2, T3, T4, T5, T6, T7, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(
                c1.decode(input), c2.decode(input), c3.decode(input),
                c4.decode(input), c5.decode(input), c6.decode(input), c7.decode(input)
            )
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
                c5.encode(output, get5.apply(value))
                c6.encode(output, get6.apply(value))
                c7.encode(output, get7.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            c5: StreamCodec<T5, B>, get5: Function<in C, out T5>,
            c6: StreamCodec<T6, B>, get6: Function<in C, out T6>,
            c7: StreamCodec<T7, B>, get7: Function<in C, out T7>,
            c8: StreamCodec<T8, B>, get8: Function<in C, out T8>,
            mk: Function8<T1, T2, T3, T4, T5, T6, T7, T8, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(
                c1.decode(input), c2.decode(input), c3.decode(input), c4.decode(input),
                c5.decode(input), c6.decode(input), c7.decode(input), c8.decode(input)
            )
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
                c5.encode(output, get5.apply(value))
                c6.encode(output, get6.apply(value))
                c7.encode(output, get7.apply(value))
                c8.encode(output, get8.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            c5: StreamCodec<T5, B>, get5: Function<in C, out T5>,
            c6: StreamCodec<T6, B>, get6: Function<in C, out T6>,
            c7: StreamCodec<T7, B>, get7: Function<in C, out T7>,
            c8: StreamCodec<T8, B>, get8: Function<in C, out T8>,
            c9: StreamCodec<T9, B>, get9: Function<in C, out T9>,
            mk: Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(
                c1.decode(input), c2.decode(input), c3.decode(input), c4.decode(input),
                c5.decode(input), c6.decode(input), c7.decode(input), c8.decode(input), c9.decode(input)
            )
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
                c5.encode(output, get5.apply(value))
                c6.encode(output, get6.apply(value))
                c7.encode(output, get7.apply(value))
                c8.encode(output, get8.apply(value))
                c9.encode(output, get9.apply(value))
            }
        }

        @JvmStatic
        public fun <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> composite(
            c1: StreamCodec<T1, B>, get1: Function<in C, out T1>,
            c2: StreamCodec<T2, B>, get2: Function<in C, out T2>,
            c3: StreamCodec<T3, B>, get3: Function<in C, out T3>,
            c4: StreamCodec<T4, B>, get4: Function<in C, out T4>,
            c5: StreamCodec<T5, B>, get5: Function<in C, out T5>,
            c6: StreamCodec<T6, B>, get6: Function<in C, out T6>,
            c7: StreamCodec<T7, B>, get7: Function<in C, out T7>,
            c8: StreamCodec<T8, B>, get8: Function<in C, out T8>,
            c9: StreamCodec<T9, B>, get9: Function<in C, out T9>,
            c10: StreamCodec<T10, B>, get10: Function<in C, out T10>,
            c11: StreamCodec<T11, B>, get11: Function<in C, out T11>,
            mk: Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, C>
        ): StreamCodec<C, B> = object : StreamCodec<C, B> {
            override fun decode(input: B): C = mk.apply(
                c1.decode(input), c2.decode(input), c3.decode(input), c4.decode(input),
                c5.decode(input), c6.decode(input), c7.decode(input), c8.decode(input),
                c9.decode(input), c10.decode(input), c11.decode(input)
            )
            override fun encode(output: B, value: C) {
                c1.encode(output, get1.apply(value))
                c2.encode(output, get2.apply(value))
                c3.encode(output, get3.apply(value))
                c4.encode(output, get4.apply(value))
                c5.encode(output, get5.apply(value))
                c6.encode(output, get6.apply(value))
                c7.encode(output, get7.apply(value))
                c8.encode(output, get8.apply(value))
                c9.encode(output, get9.apply(value))
                c10.encode(output, get10.apply(value))
                c11.encode(output, get11.apply(value))
            }
        }
    }

    public interface CodecOperation<PV, PB, TV> {
        public fun apply(base: StreamCodec<PV, PB>): StreamCodec<TV, PB>
    }

    public fun <O> apply(operation: CodecOperation<V, B, O>): StreamCodec<O, B> {
        return operation.apply(this)
    }

    public fun <O> map(
        toOut: Function<in V, out O>,
        fromOut: Function<in O, out V>
    ): StreamCodec<O, B> = object : StreamCodec<O, B> {
        override fun decode(input: B): O = toOut.apply(this@StreamCodec.decode(input))
        override fun encode(output: B, value: O) = this@StreamCodec.encode(output, fromOut.apply(value))
    }

    public fun <O : ByteBuf> mapStream(
        map: Function<in O, out B>
    ): StreamCodec<V, O> = object : StreamCodec<V, O> {
        override fun decode(input: O): V = this@StreamCodec.decode(map.apply(input))
        override fun encode(output: O, value: V) = this@StreamCodec.encode(map.apply(output), value)
    }

    public fun <U> dispatch(
        toParent: Function<in U, out V>,
        select: Function<in V, out StreamCodec<out U, in B>>
    ): StreamCodec<U, B> = object : StreamCodec<U, B> {
        override fun decode(input: B): U {
            val v = this@StreamCodec.decode(input)
            val branch = select.apply(v)
            return (branch as StreamCodec<U, B>).decode(input)
        }

        override fun encode(output: B, value: U) {
            val v = toParent.apply(value)
            val branch = select.apply(v)
            this@StreamCodec.encode(output, v)
            (branch as StreamCodec<U, B>).encode(output, value)
        }
    }

    public fun <S : B> cast(): StreamCodec<V, S> = this as StreamCodec<V, S>
}
