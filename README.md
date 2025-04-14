# OmniCore

Your new favourite Minecraft multi-versioning utility library.

---

[![Discord Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/social/discord-singular_64h.png)](https://s.deftu.dev/discord)
[![Ko-Fi Badge](https://raw.githubusercontent.com/intergrav/devins-badges/v2/assets/cozy/donate/kofi-singular_64h.png)](https://s.deftu.dev/kofi)

---

## Setup

### Repository


<details>
    <summary>Groovy (.gradle)</summary>

```gradle
maven {
    name = "Deftu Releases"
    url = "https://maven.deftu.dev/releases"
}
```
</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```kotlin
maven(url = "https://maven.deftu.dev/releases") {
    name = "Deftu Releases"
}
```
</details>

### Dependency

![Repository badge](https://maven.deftu.dev/api/badge/latest/releases/dev/deftu/omnicore-1.8.9-forge?color=C33F3F&name=OmniCore)

<details>
    <summary>Minecraft versions & mod loaders</summary>

- 1.21.4 NeoForge    (1.21.4-neoforge)
- 1.21.4 Fabric      (1.21.4-fabric)
- 1.21.1 NeoForge    (1.21.1-neoforge)
- 1.21.1 Fabric      (1.21.1-fabric)
- 1.20.6 NeoForge    (1.20.6-neoforge)
- 1.20.6 Fabric      (1.20.6-fabric)
- 1.20.4 NeoForge    (1.20.4-neoforge)
- 1.20.4 Forge       (1.20.4-forge)
- 1.20.4 Fabric      (1.20.4-fabric)
- 1.20.1 Forge       (1.20.1-forge)
- 1.20.1 Fabric      (1.20.1-fabric)
- 1.19.4 Forge       (1.19.4-forge)
- 1.19.4 Fabric      (1.19.4-fabric)
- 1.19.2 Forge       (1.19.2-forge)
- 1.19.2 Fabric      (1.19.2-fabric)
- 1.18.2 Forge       (1.18.2-forge)
- 1.18.2 Fabric      (1.18.2-fabric)
- 1.17.1 Forge       (1.17.1-forge)
- 1.17.1 Fabric      (1.17.1-fabric)
- 1.16.5 Forge       (1.16.5-forge)
- 1.16.5 Fabric      (1.16.5-fabric)
- 1.12.2 Forge       (1.12.2-forge)
- 1.8.9  Forge       (1.8.9-forge)

</details>

<details>
    <summary>Groovy (.gradle)</summary>

```gradle
modImplementation "dev.deftu:omnicore-<minecraft>-<loader>:<version>"
```

</details>

<details>
    <summary>Kotlin (.gradle.kts)</summary>

```gradle
implementation("dev.deftu:omnicore-<minecraft>-<loader>:<version>")
```

</details>

---

## Migrations

- [Migrating from 0.25.0 to 0.26.0](./migrations/0.25.0-to-0-26.0.md)

---

## Credits

- [Essential.gg](https://github.com/EssentialGG) ([Website](https://essential.gg))
  - Shader implementations across different versions ([UniversalCraft](https://github.com/EssentialGG/UniversalCraft))

---

[![BisectHosting](https://www.bisecthosting.com/partners/custom-banners/8fb6621b-811a-473b-9087-c8c42b50e74c.png)](https://s.deftu.dev/bisect)

---

**This project is licensed under [LGPL-3.0][lgpl]**\
**&copy; 2024 Deftu**

[lgpl]: https://www.gnu.org/licenses/lgpl-3.0.en.html
