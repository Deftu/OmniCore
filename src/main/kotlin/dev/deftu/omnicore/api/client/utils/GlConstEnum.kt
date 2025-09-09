package dev.deftu.omnicore.api.client.utils

public interface GlConstEnum {
    public val const: Int

    public companion object {
        @JvmStatic
        public fun <E : GlConstEnum> findOrNull(all: Collection<E>, const: Int): E? {
            return all.associateBy(GlConstEnum::const)[const]
        }

        @JvmStatic
        public fun <E : GlConstEnum> findOrThrow(clz: Class<E>, all: Collection<E>, const: Int): E {
            return findOrNull(all, const) ?: throw IllegalArgumentException("Unknown GL const for ${clz.simpleName}: $const")
        }

        @JvmStatic
        public inline fun <reified E : GlConstEnum> findOrThrow(all: Collection<E>, const: Int): E {
            return findOrThrow(E::class.java, all, const)
        }
    }
}
