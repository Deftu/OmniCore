# Removals

Unfortunately, some older APIs have been removed or replaced with better alternatives. Below is a list of the most notable removals.

## Documentation Annotations
Both of the `@GameSide` and `@IntendedLoader` annotations have been removed, as they were not being used in any meaningful way, were causing confusion for users and were used either incorrectly or inconsistently.
If you need to indicate that a class or method is only intended to be used on a specific side or loader, it should be packaged and named appropriately to portray this.

## OmniClipboard
The `OmniClipboard` utility class has been removed, as it's functionality was very, very limited and didn't work on some operating systems.
Should you need to interact with the system clipboard, take a look at [Copycat](https://github.com/Deftu/Copycat), which uses JNI to provide a more robust and cross-platform solution.

## OmniTessellator
This API has already been deprecated for way, way too long, and has finally been removed.
You can migrate your code to use the `OmniBufferBuilder` API, which provides a much more flexible and powerful way to build vertex data.
Take a look at the [0.25.0 to 0.26.0 migration guide](../0.25.0-to-0-26.0.md) for more information on how to migrate.

## OmniTextureManager
All the functionality provided by this class has been moved into OmniCore's own texture management API, which has been made much more robust and encompasses a wider range of use cases.
