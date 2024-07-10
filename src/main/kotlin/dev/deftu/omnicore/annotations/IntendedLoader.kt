package dev.deftu.omnicore.annotations

import dev.deftu.omnicore.common.OmniLoader

/**
 * Any functionality annotated with [IntendedLoader] will only have functionality on the specified loader, or may only execute on the specified loader.
 *
 * @since 0.2.2
 * @author Deftu
 */
@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.FILE
)
public annotation class IntendedLoader(vararg val loaders: OmniLoader.LoaderType)
