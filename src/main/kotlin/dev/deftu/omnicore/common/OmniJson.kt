package dev.deftu.omnicore.common

import com.google.gson.*
import dev.deftu.omnicore.annotations.GameSide
import dev.deftu.omnicore.annotations.Side
import java.io.Reader

public object OmniJson {

    //#if MC <= 1.17.1
    //$$ private val parser = JsonParser()
    //#endif

    @JvmStatic
    @GameSide(Side.BOTH)
    @Throws(JsonSyntaxException::class)
    public fun parse(json: String): JsonElement {
        //#if MC >= 1.18.2
        return JsonParser.parseString(json)
        //#else
        //$$ return parser.parse(json)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    @Throws(JsonSyntaxException::class)
    public fun parse(reader: Reader) : JsonElement {
        //#if MC >= 1.18.2
        return JsonParser.parseReader(reader)
        //#else
        //$$ return parser.parse(reader)
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun parseSafe(json: String): JsonElement? {
        return try {
            parse(json)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun parseSafe(reader: Reader): JsonElement? {
        return try {
            parse(reader)
        } catch (e: JsonSyntaxException) {
            null
        }
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun newGsonBuilder(gson: Gson): GsonBuilder {
        //#if MC >= 1.18.2
        return gson.newBuilder()
        //#else
        //$$ val builder = GsonBuilder()
        //$$
        //$$ try {
        //$$     val namingField = Gson::class.java.getDeclaredField("fieldNamingPolicy")
        //$$     namingField.isAccessible = true
        //$$     val namingStrategy = namingField.get(this) as FieldNamingStrategy
        //$$     builder.setFieldNamingStrategy(namingStrategy)
        //$$
        //$$     val factoriesField = Gson::class.java.getDeclaredField("factories")
        //$$     factoriesField.isAccessible = true
        //$$     @Suppress("UNCHECKED_CAST")
        //$$     val factories = factoriesField.get(this) as List<TypeAdapterFactory>
        //$$     factories.forEach { builder.registerTypeAdapterFactory(it) }
        //$$
        //$$     val serializeNullsField = Gson::class.java.getDeclaredField("serializeNulls")
        //$$     serializeNullsField.isAccessible = true
        //$$     if (serializeNullsField.getBoolean(this)) {
        //$$         builder.serializeNulls()
        //$$     }
        //$$
        //$$     val complexMapKeyField = Gson::class.java.getDeclaredField("complexMapKeySerialization")
        //$$     complexMapKeyField.isAccessible = true
        //$$     if (complexMapKeyField.getBoolean(this)) {
        //$$         builder.enableComplexMapKeySerialization()
        //$$     }
        //$$
        //$$     val nonExecutableField = Gson::class.java.getDeclaredField("generateNonExecutableJson")
        //$$     nonExecutableField.isAccessible = true
        //$$     if (nonExecutableField.getBoolean(this)) {
        //$$         builder.generateNonExecutableJson()
        //$$     }
        //$$
        //$$     val htmlSafeField = Gson::class.java.getDeclaredField("htmlSafe")
        //$$     htmlSafeField.isAccessible = true
        //$$     if (!htmlSafeField.getBoolean(this)) {
        //$$         builder.disableHtmlEscaping()
        //$$     }
        //$$
        //$$     val prettyPrintingField = Gson::class.java.getDeclaredField("prettyPrinting")
        //$$     prettyPrintingField.isAccessible = true
        //$$     if (prettyPrintingField.getBoolean(this)) {
        //$$         builder.setPrettyPrinting()
        //$$     }
        //$$
        //$$     val datePatternField = Gson::class.java.getDeclaredField("datePattern")
        //$$     datePatternField.isAccessible = true
        //$$     val datePattern = datePatternField.get(this) as? String
        //$$     if (datePattern != null) {
        //$$         builder.setDateFormat(datePattern)
        //$$     } else {
        //$$         val dateStyleField = Gson::class.java.getDeclaredField("dateStyle")
        //$$         dateStyleField.isAccessible = true
        //$$         val dateStyle = dateStyleField.getInt(this)
        //$$         val timeStyleField = Gson::class.java.getDeclaredField("timeStyle")
        //$$         timeStyleField.isAccessible = true
        //$$         val timeStyle = timeStyleField.getInt(this)
        //$$         builder.setDateFormat(dateStyle, timeStyle)
        //$$     }
        //$$ } catch (e: Exception) {
        //$$     throw RuntimeException("Failed to backport newBuilder from Gson", e)
        //$$ }
        //$$
        //$$ return builder
        //#endif
    }

    @JvmStatic
    @GameSide(Side.BOTH)
    public fun withPrettyPrinting(gson: Gson): Gson {
        return gson.newGsonBuilder()
            .setPrettyPrinting()
            .create()
    }

}

public val Gson.asBuilder: GsonBuilder
    get() = OmniJson.newGsonBuilder(this)

public val Gson.pretty: Gson
    get() = OmniJson.withPrettyPrinting(this)

public fun Gson.newGsonBuilder(): GsonBuilder {
    return OmniJson.newGsonBuilder(this)
}

public fun Gson.withPrettyPrinting(): Gson {
    return OmniJson.withPrettyPrinting(this)
}
