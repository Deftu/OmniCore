# Separation of Concerns

As of 1.0.0, the entire project has been restructured to follow the principle of Separation of Concerns.
This means that different functionalities have been modularized into distinct packaging and naming conventions.

All APIs have been standardized into the `dev.deftu.omnicore.api` package, and then grouped further by their physical side requirements:
- `dev.deftu.omnicore.api` for common APIs, available on the client-side, integrated server, and dedicated server.
- `dev.deftu.omnicore.api.client` for client-side only APIs.
- `dev.deftu.omnicore.api.server` for server-side only APIs, available ONLY on the dedicated server.

Furthermore, all internal implementations (and thus, wrapped game or rendering framework concepts) have been moved to the `dev.deftu.omnicore.internal` package, and then grouped similarly to the APIs.
All internal workings remain public to allow for advanced usage, but are not guaranteed to be stable and may change without notice.
They are also marked with the `@ApiStatus.Internal` annotation to indicate that they are not part of the stable API.

The majority of the more advanced API features have now been split into more extensible interfaces, allowing for more flexible implementations, better consistency across different platforms, and faster mock testing where necessary.
