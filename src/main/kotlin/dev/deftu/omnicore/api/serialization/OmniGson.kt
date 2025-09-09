@file:JvmName("OmniGson")

package dev.deftu.omnicore.api.serialization

import com.google.gson.Gson
import com.google.gson.GsonBuilder

//#if MC < 1.18.2
//$$ import com.google.gson.FieldNamingStrategy
//$$ import com.google.gson.TypeAdapterFactory
//#endif

public val Gson.asBuilder: GsonBuilder
    get() = newGsonBuilder()

public val Gson.pretty: Gson
    get() = withPrettyPrinting()

public fun Gson.newGsonBuilder(): GsonBuilder {
    //#if MC >= 1.18.2
    return newBuilder()
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

public fun Gson.withPrettyPrinting(): Gson {
    return newGsonBuilder()
        .setPrettyPrinting()
        .create()
}
