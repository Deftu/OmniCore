package dev.deftu.omnicore.api.client.options

import dev.deftu.omnicore.api.client.client

public object OmniVideoSettings {
    public object Capabilities {
        @JvmStatic
        public val isSimulationDistanceSupported: Boolean
            get() {
                //#if MC >= 1.18.2
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isEntityDistanceSupported: Boolean
            get() {
                //#if MC >= 1.17.1
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isAttackIndicatorSupported: Boolean
            get() {
                //#if MC >= 1.9
                return true
                //#else
                //$$ return false
                //#endif
            }

        @JvmStatic
        public val isBiomeBlendSupported: Boolean
            get() {
                //#if MC >= 1.13
                return true
                //#else
                //$$ return false
                //#endif
            }
    }

    @JvmStatic
    public val renderDistance: Int
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.renderDistance())
            //#else
            //$$ return unwrap(options.renderDistance)
            //#endif
        }

    @JvmStatic
    public val simulationDistance: Int
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.simulationDistance())
            //#elseif MC >= 1.18.2
            //$$ return unwrap(options.simulationDistance)
            //#else
            //$$ return 0 // Inconsistent between singleplayer and multiplayer so we'll just return 0
            //#endif
        }

    @JvmStatic
    public val entityDistance: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.entityDistanceScaling())
            //#elseif MC >= 1.17.1
            //$$ return unwrap(options.entityDistanceScaling).toDouble()
            //#else
            //$$ return 0.0 // Inconsistent between singleplayer and multiplayer so we'll just return 0
            //#endif
        }

    @JvmStatic
    public val maxFps: Int
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.framerateLimit())
            //#else
            //$$ return unwrap(options.framerateLimit)
            //#endif
        }

    @JvmStatic
    public val cloudRenderMode: CloudRenderMode
        get() {
            //#if MC >= 1.19.2
            return CloudRenderMode.from(unwrap(options.cloudStatus()))
            //#elseif MC >= 1.16.5
            //$$ return CloudRenderMode.from(options.cloudsType)
            //#else
            //$$ return CloudRenderMode.from(options.clouds)
            //#endif
        }

    @JvmStatic
    public val renderMode: RenderMode
        get() {
            //#if MC >= 1.21.11
            //$$ return RenderMode.from(unwrap(options.graphicsPreset()))
            //#elseif MC >= 1.19.2
            return RenderMode.from(unwrap(options.graphicsMode()))
            //#elseif MC >= 1.16.5
            //$$ return RenderMode.from(unwrap(options.graphicsMode))
            //#else
            //$$ return if (options.fancyGraphics) {
            //$$     RenderMode.FANCY
            //$$ } else {
            //$$     RenderMode.FAST
            //$$ }
            //#endif
        }

    @JvmStatic
    public val smoothLightingMode: SmoothLightingMode
        get() {
            //#if MC >= 1.19.4
            return if (unwrap(options.ambientOcclusion())) {
                SmoothLightingMode.MAXIMUM
            } else {
                SmoothLightingMode.OFF
            }
            //#elseif MC >= 1.19.2
            //$$ return SmoothLightingMode.from(unwrap(options.ambientOcclusion()).id)
            //#elseif MC >= 1.16.5
            //$$ return SmoothLightingMode.from(unwrap(options.ambientOcclusion).id)
            //#else
            //$$ return SmoothLightingMode.from(options.ambientOcclusion)
            //#endif
        }

    @JvmStatic
    public val chunkBuildingMode: ChunkBuildingMode
        get() {
            //#if MC >= 1.19.2
            return ChunkBuildingMode.from(unwrap(options.prioritizeChunkUpdates()))
            //#elseif MC >= 1.18.2
            //$$ return ChunkBuildingMode.from(unwrap(options.prioritizeChunkUpdates))
            //#else
            //$$ return ChunkBuildingMode.NEARBY
            //#endif
        }

    @JvmStatic
    public val mipmapLevels: Int
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.mipmapLevels())
            //#else
            //$$ return unwrap(options.mipmapLevels)
            //#endif
        }

    @JvmStatic
    public val attackIndicator: AttackIndicatorPosition
        get() {
            //#if MC >= 1.19.2
            return AttackIndicatorPosition.from(unwrap(options.attackIndicator()))
            //#elseif MC >= 1.9
            //$$ return AttackIndicatorPosition.from(unwrap(options.attackIndicator))
            //#else
            //$$ return AttackIndicatorPosition.OFF // Not supported in versions before 1.9
            //#endif
        }

    @JvmStatic
    public val biomeBlendRadius: Int
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.biomeBlendRadius())
            //#elseif MC >= 1.13
            //$$ return unwrap(options.biomeBlendRadius)
            //#else
            //$$ return 0 // Not supported in versions before 1.13
            //#endif
        }

    @JvmStatic
    public val isVsyncEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.enableVsync())
            //#else
            //$$ return unwrap(options.enableVsync)
            //#endif
        }

    @JvmStatic
    public val isEntityShadowEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.entityShadows())
            //#else
            //$$ return unwrap(options.entityShadows)
            //#endif
        }

    @JvmStatic
    public val isForceUnicodeFont: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.forceUnicodeFont())
            //#else
            //$$ return unwrap(options.forceUnicodeFont)
            //#endif
        }

    @JvmStatic
    public val isFullscreen: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.fullscreen())
            //#else
            //$$ return unwrap(options.fullscreen)
            //#endif
        }

    @JvmStatic
    public val isViewBobbingEnabled: Boolean
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.bobView())
            //#else
            //$$ return unwrap(options.bobView)
            //#endif
        }

    @JvmStatic
    public val fov: Int
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.fov()).toInt()
            //#else
            //$$ return unwrap(options.fov).toInt()
            //#endif
        }

    @JvmStatic
    public val isHudHidden: Boolean
        get() = options.hideGui

    @JvmStatic
    public val isDebugRendering: Boolean
        get() {
            //#if MC >= 1.20.4
            return client.debugOverlay.showDebugScreen()
            //#else
            //$$ return options.renderDebug
            //#endif
        }
}
