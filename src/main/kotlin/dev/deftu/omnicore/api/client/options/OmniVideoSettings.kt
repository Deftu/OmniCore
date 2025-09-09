package dev.deftu.omnicore.api.client.options

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
        get() = unwrap(options.viewDistance)

    @JvmStatic
    public val simulationDistance: Int
        get() {
            //#if MC >= 1.18.2
            return unwrap(options.simulationDistance)
            //#else
            //$$ return 0 // Inconsistent between singleplayer and multiplayer so we'll just return 0
            //#endif
        }

    @JvmStatic
    public val entityDistance: Double
        get() {
            //#if MC >= 1.19.2
            return unwrap(options.entityDistanceScaling)
            //#elseif MC >= 1.17.1
            //$$ return options.entityDistanceScaling.toDouble()
            //#else
            //$$ return 0.0 // Inconsistent between singleplayer and multiplayer so we'll just return 0
            //#endif
        }

    @JvmStatic
    public val maxFps: Int
        get() = unwrap(options.maxFps)

    @JvmStatic
    public val cloudRenderMode: CloudRenderMode
        get() = CloudRenderMode.from(unwrap(options.cloudRenderMode))

    @JvmStatic
    public val renderMode: RenderMode
        get() {
            //#if MC >= 1.16.5
            return RenderMode.from(unwrap(options.graphicsMode))
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
            return if (unwrap(options.ao)) {
                SmoothLightingMode.MAXIMUM
            } else {
                SmoothLightingMode.OFF
            }
            //#elseif MC >= 1.16.5
            //$$ return SmoothLightingMode.from(unwrap(options.ambientOcclusion()).id)
            //#else
            //$$ return SmoothLightingMode.from(options.ambientOcclusion)
            //#endif
        }

    @JvmStatic
    public val chunkBuildingMode: ChunkBuildingMode
        get() {
            //#if MC >= 1.18.2
            return ChunkBuildingMode.from(unwrap(options.chunkBuilderMode))
            //#else
            //$$ return ChunkBuildingMode.NEARBY
            //#endif
        }

    @JvmStatic
    public val mipmapLevels: Int
        get() = unwrap(options.mipmapLevels)

    @JvmStatic
    public val attackIndicator: AttackIndicatorPosition
        get() {
            //#if MC >= 1.9
            return AttackIndicatorPosition.from(unwrap(options.attackIndicator))
            //#else
            //$$ return AttackIndicatorPosition.OFF // Not supported in versions before 1.9
            //#endif
        }

    @JvmStatic
    public val biomeBlendRadius: Int
        get() {
            //#if MC >= 1.13
            return unwrap(options.biomeBlendRadius)
            //#else
            //$$ return 0 // Not supported in versions before 1.13
            //#endif
        }

    @JvmStatic
    public val isVsyncEnabled: Boolean
        get() = unwrap(options.enableVsync)

    @JvmStatic
    public val isEntityShadowEnabled: Boolean
        get() = unwrap(options.entityShadows)

    @JvmStatic
    public val isForceUnicodeFont: Boolean
        get() = unwrap(options.forceUnicodeFont)

    @JvmStatic
    public val isFullscreen: Boolean
        get() = unwrap(options.fullscreen)

    @JvmStatic
    public val isViewBobbingEnabled: Boolean
        get() = unwrap(options.bobView)

    @JvmStatic
    public val fov: Int
        get() = unwrap(options.fov)

    @JvmStatic
    public val isHudHidden: Boolean
        get() = options.hudHidden

    @JvmStatic
    public val isDebugRendering: Boolean
        get() {
            //#if MC >= 1.20.4
            return OmniClient.getInstance().debugHud.shouldShowDebugHud()
            //#else
            //$$ return options.renderDebug
            //#endif
        }
}
