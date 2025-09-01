# Kotlinification
As of 1.0.0, OmniCore's codebase has been migrated to a more Kotlin-focused design and implementation.
This means that, while Java is still supported as a first-class citizen, the codebase itself and it's extended support from here on will be primarily focused on the Kotlin design philosophy and idioms.

Now, OmniCore was always written in Kotlin, and would use Kotlin features where possible, but this release takes it a huge step further:
- APIs such as `OmniClient` have not only been decoupled into smaller tidbits, but now only expose top-level properties and functions in Kotlin, and aren't full objects anymore. (Still usable via that Java getter syntax, of course!)
- Most extensions upon vanilla concepts have been turned into actual Kotlin extension functions rather than static utility methods, and again, will still be usable from Java the same as before.
- Everywhere possible, utilities have been made inline, slightly improving performance and reducing boilerplate.

Measures are still being taken to ensure that Java support remains first-class, and we will continue to do so in future releases.
However, deliberate design decisions have been made to ensure that the Kotlin experience is as smooth as possible, even down to having different method names for Java and Kotlin where it makes sense to do so.
A good example of this would be the profiler API:
```kotlin
profiled("my_profiler_section") {
    // Run your code
}
```
```java
OmniProfiler.withProfiler("my_profiler_section", () -> {
    // Run your code
});
```

Both of these examples have the exact same function definition, but are named differently to better suit the language's idioms!
```kotlin
@JvmName("withProfiler")
public fun profiled(name: String, block: Runnable) {
    try {
        try {
            swapProfiler(name)
        } catch (_: Exception) {
            startProfiler(name)
        }

        block.run()
    } finally {
        endProfiler()
    }
}
```
